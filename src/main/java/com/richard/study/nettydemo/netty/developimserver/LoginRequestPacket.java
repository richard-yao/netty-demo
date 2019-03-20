package com.richard.study.nettydemo.netty.developimserver;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author richard_xsyao
 * @date 2018/9/27 14:44
 * Description:
 */
@Data
public class LoginRequestPacket extends Packet {

    private Integer userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}