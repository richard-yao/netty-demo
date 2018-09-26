package com.richard.study.nettydemo.netty.simpleexamples;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/5 14:59
 * Description:
 */
public class EchoServer {

    private int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new EchoServer(8091).startServer();
    }

    public void startServer() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new EchoServerHandler());
                        }
                    });
            // 异步地绑定服务器, 调用sync()方法阻塞等待直到绑定完成
            ChannelFuture future = bootstrap.bind(port).sync();
            // 获取Channel的CloseFuture, 并且阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
