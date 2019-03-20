package com.richard.study.nettydemo.bio;

import com.richard.study.nettydemo.util.CustomThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/4 14:25
 * Description:
 */
public class BioServerDemo {

    private Logger logger = LoggerFactory.getLogger(BioServerDemo.class);

    private int port;

    public BioServerDemo(int port) {
        this.port = port;
    }

    /**
     * Start socket to listen specify port, when clietn connect to server, it will receiver helloword
     */
    public void initServer() throws IOException {
        ExecutorService executorService = CustomThreadPool.getThreadPool();
        // establish socket server with bind specify port
        ServerSocket server = new ServerSocket(port);
        String hello = "Hello world!";
        while (true) {
            // BIO的阻塞过程体现在线程阻塞接受连接的建立，在建立后会返回Socket对象，可以新开一个线程来处理连接
            // blocking until someone client establish connection, this method will return one Socket object
            final Socket socket = server.accept();
            // Open one thread to deal with the connection
            executorService.submit(() -> {

                try {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(hello.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
        }
    }
}
