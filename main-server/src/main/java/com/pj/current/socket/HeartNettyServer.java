package com.pj.current.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j

public class HeartNettyServer extends Thread {
    private InetSocketAddress address;

    public HeartNettyServer(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public void run() {
        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(4);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)  // 绑定线程池
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address)
                    .childHandler(new HeartChannelInitializer())//编码解码
                    .option(ChannelOption.SO_REUSEADDR, true)  //服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  //保持长连接，2小时无数据激活心跳机制

            // 绑定端口，开始接收进来的连接
             bootstrap.bind(address).addListeners(future -> {
                 log.info("心跳服务器开始监听端口：{}" ,address.getPort());
             });
            //关闭channel和块，直到它被关闭
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
