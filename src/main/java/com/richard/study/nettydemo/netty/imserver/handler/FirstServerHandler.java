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
 * @date: 2018/9/26 17:07
 * Description:
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("Server receive data: " + byteBuf.toString(Charset.forName("UTF-8")));

        ByteBuf sliceOne = byteBuf.slice();
        // The sliceOne's maxWritableBytes is limited to the byteBuf's readerIndex - writerIndex
        if (!sliceOne.isWritable()) {
            // Read all msg out
            byte[] dstArray = new byte[sliceOne.readableBytes()];
            sliceOne.readBytes(dstArray);
            System.out.println("Server receive data: " + new String(dstArray));
        }
        sliceOne.writeBytes("richard".getBytes(Charset.forName("UTF-8")));
        System.out.println("Slice one data: " + sliceOne.toString(Charset.forName("UTF-8")));

        ByteBuf duplicateOne = byteBuf.duplicate();
        duplicateOne.writeBytes("Duplicate string".getBytes(Charset.forName("UTF-8")));
        System.out.println("Duplicate one data: " + sliceOne.toString(Charset.forName("UTF-8")));

        System.out.println("Server write out data on " + new Date());
        ByteBuf outputMsg = getByteBuf(ctx);
        ctx.writeAndFlush(outputMsg);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf byteBuf = ctx.alloc().buffer();
        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "返回数据到客户端".getBytes(Charset.forName("UTF-8"));
        // 3. 填充数据到 ByteBuf
        if (byteBuf.writableBytes() >= bytes.length) {
            byteBuf.writeBytes(bytes);
        } else {
            byteBuf.writeBytes(bytes, 0, byteBuf.writableBytes());
        }
        return byteBuf;
    }
}