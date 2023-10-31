package com.gzlh.netty;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NettyServer {
    @Resource
    private NettyConfig nettyConfig;

    public void send(String msg) {
        nettyConfig.send(msg);
    }

}
