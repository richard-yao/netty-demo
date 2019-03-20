package com.richard.study.test.bio;

import com.richard.study.nettydemo.bio.BioServerDemo;
import com.richard.study.nettydemo.util.CustomThreadPool;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/4 15:14
 * Description:
 */
public class BioServerTest {

    @Test
    public void testCustomThreadPool() {
        ExecutorService executorService = CustomThreadPool.getThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println("Thread-name: " + Thread.currentThread().getName());
            });
        }
        executorService.shutdown();
    }

    @Ignore
    @Test
    public void testBioServer() {
        BioServerDemo demo = new BioServerDemo(8083);
        try {
            demo.initServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
