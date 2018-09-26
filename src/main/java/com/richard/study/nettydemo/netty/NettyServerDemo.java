package com.richard.study.nettydemo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/4 17:17
 * Description:
 */
public class NettyServerDemo {

    private Logger logger = LoggerFactory.getLogger(NettyServerDemo.class);

    private int port;

    public NettyServerDemo(int port) {
        this.port = port;
    }

    public void initServer() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hello world everyone!".getBytes("UTF-8")));
        EventLoopGroup loopGroup = new OioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(loopGroup)
                .channel(OioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ctx.writeAndFlush(byteBuf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                ctx.fireChannelInactive();
                                logger.info("Client lost connection! {}", ctx.channel().localAddress().toString());
                            }
                        });
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                loopGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
