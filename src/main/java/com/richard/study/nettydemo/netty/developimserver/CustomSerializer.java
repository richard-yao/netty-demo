package com.richard.study.nettydemo.netty.developimserver;

/**
 * Created with IntelliJ IDEA.
 *
 * @author richard_xsyao
 * @date 2018/9/27 14:46
 * Description:
 */
public interface CustomSerializer {

    /**
     * 序列化算法定义
     *
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * 将对象序列化后返回
     *
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 将序列化字符转化为对象
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserializ(Class<T> clazz, byte[] bytes);
}