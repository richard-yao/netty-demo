package com.richard.study.nettydemo.netty.longconnection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by RoyDeng on 17/11/22.
 */
public class LiveServer {

    private final int port;

    private static Logger logger = LoggerFactory.getLogger(LiveServer.class);

    public LiveServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        int port = 8089;
        logger.debug("start server with port:" + port);
        new LiveServer(port).start();
    }

    public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        logger.debug("initChannel ch:" + ch);
                        ch.pipeline()
                                .addLast("decoder", new LiveDecoder())
                                .addLast("encoder", new LiveEncoder())
                                .addLast("handler", new LiveHandler());
                    }
                })
                // determining the number of connections queued
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        b.bind(port).sync();
    }
}
