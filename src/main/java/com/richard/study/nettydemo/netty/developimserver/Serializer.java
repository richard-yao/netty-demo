package com.richard.study.nettydemo.netty.developimserver;

/**
 * Created with IntelliJ IDEA.
 *
 * @author richard_xsyao
 * @date 2018/9/27 15:13
 * Description:
 */
public interface Serializer {

    byte JSON_SERIALIZER = 1;

    CustomSerializer SERIALIZER = new JsonSerializer();
}