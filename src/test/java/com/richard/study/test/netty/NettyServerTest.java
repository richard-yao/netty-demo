package com.richard.study.test.netty;

import com.richard.study.nettydemo.netty.NettyServerDemo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/4 17:34
 * Description:
 */
public class NettyServerTest {

    @Test
    public void testNettyServer() {
        NettyServerDemo serverDemo = new NettyServerDemo(8086);
        try {
            serverDemo.initServer();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private char[] charArray = {'a', 'b', 'c', 'd', 'q'};

    @Test
    public void testNettyByteBuf() {
        ByteBuf byteBuf = Unpooled.buffer(5);
        while(byteBuf.writableBytes() > 0) {
            int randomNum = RandomUtils.nextInt(0, charArray.length);
            char randomChar = charArray[randomNum];
            System.out.println("Input char: " + randomChar);
            byteBuf.writeByte((int) randomChar);
        }
        while(byteBuf.readableBytes() > 0) {
            System.out.println("Read out char: " + (char) byteBuf.readByte());
        }
    }
}
