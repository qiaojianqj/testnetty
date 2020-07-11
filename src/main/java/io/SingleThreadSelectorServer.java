package io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 多路复用之 boss accept + worker read & write 同一个线程
 */
public class SingleThreadSelectorServer {
    private ServerSocketChannel server = null;
    private Selector selector = null;
    int port = 19999;

    public void init() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        init();
        System.out.println("server start...");
        try {
            while (true) {
            Thread.sleep(1000);
            int num = selector.select(); // block until event coming
            System.out.println("selector return " + num);
            if (num > 0) {
                Iterator<SelectionKey> selectionKeyIterator =  selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    if (selectionKey.isAcceptable()) {
                        acceptHandler(selectionKey);
                    } else if (selectionKey.isReadable()) {
                        readHandler(selectionKey);
                    }
                }
            }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptHandler(SelectionKey selectionKey) {
        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
        try {
            SocketChannel client = serverChannel.accept();
            client.configureBlocking(false);
            ByteBuffer clientBuf = ByteBuffer.allocate(1024);
            client.register(selector, SelectionKey.OP_READ, clientBuf);
            System.out.println("----new client---" + client.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readHandler(SelectionKey selectionKey) {
        SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer clientBuf = (ByteBuffer) selectionKey.attachment();
        clientBuf.clear();
        try {
           while (true) {
               int num = client.read(clientBuf);
               System.out.println("----read----num----:" + num + " from client---" + client.getRemoteAddress());
               if (num > 0) {
                   clientBuf.flip();
                   while (clientBuf.hasRemaining()) {
                       client.write(clientBuf);
                   }
                   clientBuf.clear();
               } else if (num == 0) {
                   break;
               }
               else { // -1 close_wait 不加-1判断，客户端主动close会导致空轮询bug
                   System.out.println("client---" + client.getRemoteAddress() + " close" );
                   client.close();
                   break;
               }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SingleThreadSelectorServer singleThreadSelectorServer = new SingleThreadSelectorServer();
        singleThreadSelectorServer.start();
    }
}
