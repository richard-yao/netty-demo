package com.richard.study.nettydemo.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/4 16:49
 * Description:
 */
public class NioServerDemo {

    private Logger logger = LoggerFactory.getLogger(NioServerDemo.class);

    private int port;

    public NioServerDemo(int port) {
        this.port = port;
    }

    /**
     * Using java NIO for demo, NIO可以由一个线程负责和多个Channel之间通信
     */
    public void initServer() throws IOException {
        // 初始化ServerSocketChannel
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        // 开启ServerSocket并绑定端口
        ServerSocket serverSocket = socketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));
        // Create a selector
        Selector selector = Selector.open();
        // Register selector to channel
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer byteBuffer = ByteBuffer.wrap("Hello world, I am richard!".getBytes("UTF-8"));
        while(true) {
            try {
                selector.select();
            } catch(IOException e) {
                logger.error(e.getMessage(), e);
                break;
            }
            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keySet.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();
                // isAcceptable状态需要注册OP_WRITE
                if(selectionKey.isAcceptable()) {
                    ServerSocketChannel tempChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = tempChannel.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_WRITE, byteBuffer.duplicate());
                    System.out.println("Accepted connection from " + client);
                }
                if(selectionKey.isWritable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    while(buffer.hasRemaining()) {
                        if(client.write(buffer) > 0) {
                            continue;
                        }
                    }
                    client.close();
                }
                selectionKey.cancel();
                selectionKey.channel().close();
            }
        }
    }
}
