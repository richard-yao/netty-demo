package com.richard.study.test.netty;

import com.richard.study.nettydemo.netty.NettyHttpServer;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/4 18:20
 * Description:
 */
public class NettyHttpServerTest {

    @Test
    public void testHttpServer() {
        NettyHttpServer httpServer = new NettyHttpServer();
        try {
            httpServer.startHttpServer(8087);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
