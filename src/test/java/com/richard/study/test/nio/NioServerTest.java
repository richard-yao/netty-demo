package com.richard.study.test.nio;

import com.richard.study.nettydemo.nio.NioServerDemo;
import org.junit.Test;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/4 17:11
 * Description:
 */
public class NioServerTest {

    @Test
    public void testNioServer() {
        NioServerDemo serverDemo = new NioServerDemo(8085);
        try {
            serverDemo.initServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
