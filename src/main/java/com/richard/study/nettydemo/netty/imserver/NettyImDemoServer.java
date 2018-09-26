package com.richard.study.nettydemo.netty.imserver;

import com.richard.study.nettydemo.netty.imserver.handler.FirstServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/26 14:20
 * Description:
 */
public class NettyImDemoServer {

    public static void main(String[] args) {
        startServer();
    }

    /**
     * 要启动一个Netty服务端，必须要指定三类属性，分别是线程模型、IO 模型、连接读写处理逻辑
     */
    private static void startServer() {
        // bossGroup listen port and accpet new connection
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // workerGroup loop all connection and deal with the communication
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                // 使用group方法给serverBootstrap引导类配置两大线程组,指定线程模型
                .group(bossGroup, workerGroup)
                // 使用channel方法指定IO模型为NIO
                .channel(NioServerSocketChannel.class)
                // 定义后续每条连接的数据读写，业务处理逻辑
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new FirstServerHandler());
                    }
                });
        // 给每个SocketChannel添加属性，在链接中可以通过socketChannel.attr()来取得对应的值
        serverBootstrap.childAttr(AttributeKey.newInstance("clientKey"), "clientValue");

        // 设定每个连接的一些tcp底层状态
        serverBootstrap
                // 开启tcp底层心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 关闭Nagle算法，消息实时发送
                .childOption(ChannelOption.TCP_NODELAY, true);

        // 系统用于临时存放已完成三次握手的请求的队列的最大长度; 如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);

        bindPortAutomatically(serverBootstrap, 8000);
    }

    /**
     * Bind someone port available automatically
     * @param serverBootstrap
     * @param listenPort
     */
    private static void bindPortAutomatically(ServerBootstrap serverBootstrap, int listenPort) {
        // bind方法是异步的，会立即返回一个ChannelFuture回调
        ChannelFuture channelFuture = serverBootstrap.bind(listenPort);
        // 添加一个listener在ChannelFuture回调返回结果时执行
        channelFuture.addListener((future) -> {
            if(future.isSuccess()) {
                System.out.println("Bind port [" + listenPort + "] successfully!");
            } else {
                System.out.println("Bind port [" + listenPort + "] failed! Try next port...");
                bindPortAutomatically(serverBootstrap,listenPort + 1);
            }
        });
    }
}