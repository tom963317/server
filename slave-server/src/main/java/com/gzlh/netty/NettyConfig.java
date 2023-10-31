package com.gzlh.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class NettyConfig {

    @Value("${netty.host}")
    private String host;

    @Value("${netty.port}")
    private int port;

    @Autowired
    private NettyClientHandler nettyClientHandler;

    private Channel channel;

    @Bean
    public Bootstrap bootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        return new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch)  {
                        try {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast("handler", nettyClientHandler);
                        }catch (Exception e){
                            log.info("error connect:{}",e.getMessage());
                        }
                    }
                });
    }

    @Bean
    public NettyClientHandler nettyClientHandler() {
        return new NettyClientHandler(this);
    }

    public void connect() {
        ChannelFuture future = bootstrap().connect();
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                channel = future1.channel();
                log.info("连接服务器成功,{},{}",host,port);
            } else {
                log.error("-------------连接服务器失败-----------，进行重连");
                future1.channel().eventLoop().schedule(this::connect, 10, TimeUnit.SECONDS);
            }
        });
    }

    public void send(String message) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        } else {
            log.error("未建立连接，无法发送消息");
        }
    }

    public void close() {
        if (channel != null) {
            channel.close();
        }
    }
}

