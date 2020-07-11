package io;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * NIO - 设置同步非阻塞：fcntl O_NONBLOCK
 * 1个线程可以处理多个连接
 * 问题：每次循环不管有没有可读数据都得遍历read所有客户端连接
 * 解决：epoll，对应java selector
 * @author qiaojian
 */
public class NonBlockIOServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 优化：可以将clients分摊到线程池进行遍历处理
        ArrayList<SocketChannel> clients = new ArrayList<>();

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(19999));
        server.configureBlocking(false);
        System.out.println("new ServerSocketChannel 19999");
        while (true) {
            //Thread.sleep(1000);
            SocketChannel client = server.accept(); // 非阻塞，没有连接返回null
            if (client == null) {
                System.out.println("null...");
            } else {
                client.configureBlocking(false);
                System.out.println("rev " + client.socket().getInetAddress().toString() + " " + client.socket().getPort() );
                clients.add(client);
            }
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Iterator<SocketChannel> itr = clients.iterator();
            while (itr.hasNext()) {
               SocketChannel c = itr.next();
                int num = c.read(buffer); // 非阻塞，没有可读数据，返回0
                if (num > 0) {
                    System.out.println("read " + num +" data from" + c.socket().getInetAddress().toString() + " " + c.socket().getPort());
                    buffer.flip();
                    c.write(buffer);
                } else if (num == 0) {
                    System.out.println("read 0 data from" + c.socket().getInetAddress().toString() + " " + c.socket().getPort());
                    // 没有接收到数据
                } else { // -1 连接已经close掉了
                    System.out.println("ready to close: read return -1 from" + c.socket().getInetAddress().toString() + " " + c.socket().getPort());
                    c.close();
                    itr.remove();
                }
            }
        }
    }
}
