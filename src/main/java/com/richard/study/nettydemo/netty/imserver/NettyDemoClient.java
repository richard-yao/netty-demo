package com.richard.study.nettydemo.netty.imserver;

import com.richard.study.nettydemo.netty.imserver.handler.FirstClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/26 15:00
 * Description:
 */
public class NettyDemoClient {

    private static int MAX_RETRY_TIMES = 3;

    public static void main(String[] args) {
        startClient();
    }

    /**
     * default start client bootstrap to connect to remote server
     */
    private static void startClient() {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(clientGroup)
                // 2.指定IO模型为NIO
                .channel(NioSocketChannel.class)
                // 3.处理连接
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 返回的是和这条连接相关的逻辑处理链, 责任链模式
                        ch.pipeline()
                                // 添加一个逻辑处理器
                          .addLast(new FirstClientHandler());
                    }
                });
        // 设定连接服务器的超时时间, 超过指定时间SocketChannel连接自动关闭
        // Netty的处理是在调用建连之后马上启动一个延时任务，该任务在timeout时间过后执行，任务中执行关闭操作，连接建立成功之后取消这个任务，处理代码在AbstractNioUnsafe类的connect方法中
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);

        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        reconnectWithTimeDelay(bootstrap, new InetSocketAddress("localhost", 8000), MAX_RETRY_TIMES);
    }

    /**
     * 带有指数退避的客户端重连机制，最大尝试次数5, 重连间隔2s
     * @param bootstrap
     * @param socketAddress
     * @param remainRetryTimes
     */
    private static void reconnectWithTimeDelay(Bootstrap bootstrap, SocketAddress socketAddress, int remainRetryTimes) {
        ChannelFuture remoteConnection = bootstrap.connect(socketAddress);
        remoteConnection.addListener((future) -> {
           if(future.isSuccess()) {
               System.out.println("Connect to remote server successfully!");
           } else if(remainRetryTimes == 0) {
               System.out.println("Reconnet failed after " + MAX_RETRY_TIMES + " times retry!");
           } else {
               int order = (MAX_RETRY_TIMES - remainRetryTimes) + 1;
               int delay = 1 << order;
               System.out.println(new Date() + ": Connect to remote server failed! Retry times: " + remainRetryTimes);
               bootstrap.config().group().schedule(
                       () -> reconnectWithTimeDelay(bootstrap, socketAddress, remainRetryTimes - 1),
                       delay,
                       TimeUnit.SECONDS);
           }
        });
    }
}