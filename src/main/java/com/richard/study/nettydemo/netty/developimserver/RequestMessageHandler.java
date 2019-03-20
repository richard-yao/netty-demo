package com.richard.study.nettydemo.netty.developimserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created with IntelliJ IDEA.
 *
 * @author richard_xsyao
 * @date 2018/9/28 11:28
 * Description: 通过继承SimpleChannelInboundHandler时指定泛型的方式来在pipeline中确定处理消息类型的Handler用以减少不必要的if/else判断
 */
public class RequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {

    }
}