package com.pj.current.socket;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;

import com.pj.project.bin_card.BinCard;
import com.pj.project.bin_card.BinCardService;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {


    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    private final static TimedCache<String, Integer> UP_COUNT = CacheUtil.newTimedCache(500);
    private static final TimedCache<String, List<ChannelHandlerContext>> IP_CHANNEL_MAP = CacheUtil.newTimedCache(2000);

    private void addIpMap(String ip, ChannelHandlerContext context) {
        List<ChannelHandlerContext> list = IP_CHANNEL_MAP.get(ip);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
        }
        list.add(context);
        IP_CHANNEL_MAP.put(ip, list);
    }

    private void breakCtx(String ip) {
        List<ChannelHandlerContext> list = IP_CHANNEL_MAP.get(ip);
        if (list != null) {
            list.parallelStream().forEach(ChannelOutboundInvoker::close);
        }
    }

    /**
     * @param ctx
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //上线时同步时间===========
        CHANNEL_MAP.put(ctx.channel().id().asShortText(), ctx);
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        Integer count = UP_COUNT.get(clientIp);
        if (count == null) {
            count = 0;
        }
        if (count >= 4) {
            breakCtx(clientIp);
            ctx.close();
            return;
        }
        count = count + 1;
        UP_COUNT.put(clientIp, count);
        addIpMap(clientIp, ctx);
        StaticLog.info("up:{},{},{}", ctx.channel().id().asShortText(), clientIp, CHANNEL_MAP.size());

        CHANNEL_MAP.put("test",ctx);
    }

    /**
     * @param ctx
     * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        CHANNEL_MAP.remove(ctx.channel().id().asShortText());
        ctx.fireChannelInactive();
//        StaticLog.info("down:{}", ctx.channel().id().asShortText());
    }

    TimedCache<String, String> CONTENT_CACHE = CacheUtil.newTimedCache(5000);

    /**
     * @param ctx
     * @DESCRIPTION: 有客户端发消息会触发此函数
     * @return: void
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String str = msg.toString();
        if (StrUtil.equals(str, "heartbeat")) {
            return;
        }
        try {
            String channelId = ctx.channel().id().asShortText();
            if (!StrUtil.endWith(str, "}") && StrUtil.startWith(str, "{")) {
                CONTENT_CACHE.put(channelId, str);
                return;
            } else if (!StrUtil.endWith(str, "}") && !StrUtil.startWith(str, "{")) {
                String content = CONTENT_CACHE.get(channelId);
                content += str;
                CONTENT_CACHE.put(channelId, content);
                return;
            } else if (StrUtil.endWith(str, "}")) {
                String content = CONTENT_CACHE.get(channelId);
                if (StrUtil.isEmpty(content)) {
                    content = "";
                }
                str = content + str;
                CONTENT_CACHE.remove(channelId);
            }
            BinCard card = JSONUtil.toBean(str, BinCard.class);
            BinCardService binCardService = SpringUtil.getBean(BinCardService.class);
            CHANNEL_MAP.put(card.getSysName(), ctx);
            binCardService.handler(card);

        } catch (Exception e) {
          log.error("error:{}", e.getMessage());
          e.printStackTrace();
        }

    }

    /**
     * @param msg 需要发送的消息内容
     * @DESCRIPTION: 服务端给客户端发送消息
     * @return: void
     */
    public static void channelWrite(String sysName, String msg) {
        ChannelHandlerContext cache = CHANNEL_MAP.get(sysName);
        if (cache == null) {
            return;
        }
        try {
            String content = JSONUtil.toJsonStr(msg);
            //将客户端的信息直接返回写入ctx
            cache.writeAndFlush(content);
        } catch (Exception e) {
            StaticLog.info("send error:{}", e.getMessage());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 发送心跳消息
            ctx.writeAndFlush("heartbeat");
        } else {
            super.userEventTriggered(ctx, evt);
        }
        String socketString = ctx.channel().remoteAddress().toString();

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("Client: " + socketString + " READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client: " + socketString + " ALL_IDLE 总超时");
                ctx.disconnect();
            }
        }
    }

    /**
     * @param ctx
     * @DESCRIPTION: 发生异常会触发此函数
     * @return: void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String id = ctx.channel().id().asShortText();
        log.error("socket error:{},{}", cause.getMessage(), id);
        ChannelHandlerContext cache = CHANNEL_MAP.get(id);
        if (cache != null) {
            cache.close();
            cache.disconnect();
            ctx.close();
        }
        CHANNEL_MAP.remove(id);
    }

}
