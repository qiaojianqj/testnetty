package io;

import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * nio 客户端注册 connect + read + write事件
 * nio 服务端注册 accept + read + write事件
 * 关于connect事件和write事件用法参考 https://blog.csdn.net/weixin_34072458/article/details/88662239
 */
public class NioClient {

    private static void sendMsg(SocketChannel socketChannel) throws IOException {
        String clientMsg = "hello, i'm client";
        ByteBuffer clientBuf = ByteBuffer.allocate(1024);
        clientBuf.put(clientMsg.getBytes());
        clientBuf.flip();
        socketChannel.write(clientBuf);
        if (!clientBuf.hasRemaining()) {
            System.out.println("Client send msg success");
        }
    }

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();

        if (socketChannel.connect(new InetSocketAddress(19999))) {
            // 连接成功，注册read事件，发送消息到服务端
            socketChannel.register(selector, SelectionKey.OP_READ);
            sendMsg(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

        while (true) {
            int num = selector.select();
            if (num > 0) {
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    if (selectionKey.isConnectable()) {
                        SocketChannel socketChannel1 = ((SocketChannel)selectionKey.channel());
                        socketChannel1.finishConnect();
                        socketChannel1.register(selector, SelectionKey.OP_READ);
                        sendMsg(socketChannel1);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel2 = ((SocketChannel)selectionKey.channel());
                        ByteBuffer readBuf = ByteBuffer.allocate(1024);
                        int readBytes = socketChannel2.read(readBuf);
                        if (readBytes > 0) {
                            readBuf.flip();
                            byte[] receivedBytes = new byte[readBuf.remaining()];
                            readBuf.get(receivedBytes);
                            System.out.println("received from server: " + new String(receivedBytes, StandardCharsets.UTF_8));
                        }
                    }
                }
            } else {
                System.out.println("select return " + num);
            }
        }
    }
}
