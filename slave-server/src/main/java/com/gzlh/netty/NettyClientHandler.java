package com.gzlh.netty;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.gzlh.cache.CacheManager;
import com.gzlh.entity.AgreeBO;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<String>{


    private NettyConfig nettyConfig;

    public NettyClientHandler(NettyConfig nettyConfig) {
        this.nettyConfig = nettyConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("客户端收到消息：" + msg);
        AgreeBO bo = JSONUtil.toBean(msg, AgreeBO.class);
        CacheManager.setCache(bo.getFingerprint(), bo);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接断开，进行重连");
        nettyConfig.connect();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 在客户端重新连接成功后，重新设置 ChannelPipeline

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

