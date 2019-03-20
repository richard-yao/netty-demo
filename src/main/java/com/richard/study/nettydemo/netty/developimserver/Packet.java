package com.richard.study.nettydemo.netty.developimserver;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author richard_xsyao
 * @date 2018/9/27 14:38
 * Description:
 */
@Data
public abstract class Packet {

    /**
     * 协议版本，保留字段
     */
    private Byte version = 1;

    /**
     * 获取指令字段
     *
     * @return
     */
    public abstract Byte getCommand();
}