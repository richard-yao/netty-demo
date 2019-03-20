package com.richard.study.nettydemo.netty.developimserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 *
 * @author richard_xsyao
 * @date 2018/9/27 14:50
 * Description: 自定义基础序列化方法，使用fastjson
 */
public class JsonSerializer implements CustomSerializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSONObject.toJSONBytes(object);
    }

    @Override
    public <T> T deserializ(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}