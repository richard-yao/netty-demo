package com.richard.study.nettydemo.netty.imserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/26 16:59
 * Description:
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("Client receive data: " + byteBuf.toString(Charset.forName("UTF-8")));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client write out data on " + new Date());
        ByteBuf byteBuf = getByteBuf(ctx);
        // 写数据
        ctx.writeAndFlush(byteBuf);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf byteBuf = ctx.alloc().buffer();
        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "啦啦啦来了1111".getBytes(Charset.forName("UTF-8"));
        // 3. 填充数据到 ByteBuf
        if(byteBuf.writableBytes() >= bytes.length) {
            byteBuf.writeBytes(bytes);
        } else {
            byteBuf.writeBytes(bytes, 0, byteBuf.writableBytes());
        }
        return byteBuf;
    }
}