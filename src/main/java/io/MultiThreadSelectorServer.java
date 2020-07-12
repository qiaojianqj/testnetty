package io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  多路复用之：
 *  boss accept 一个线程
 *  worker read & write 两个线程
 * @author qiaojian
 */
public class MultiThreadSelectorServer {
    private ServerSocketChannel server;
    // 1个selector对应linux 1个epoll
    private Selector bossSelector;
    private Selector workerSelector1;
    private Selector workerSelector2;

    private void init() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(19999));
            bossSelector = Selector.open();
            server.register(bossSelector, SelectionKey.OP_ACCEPT);
            workerSelector1 = Selector.open();
            workerSelector2 = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MultiThreadSelectorServer multiThreadSelectorServer = new MultiThreadSelectorServer();
        multiThreadSelectorServer.init();
        NettyThread bossThread = new NettyThread(multiThreadSelectorServer.bossSelector, 2);
        NettyThread workerThread1 = new NettyThread(multiThreadSelectorServer.workerSelector1);
        NettyThread workerThread2 = new NettyThread(multiThreadSelectorServer.workerSelector2);
        bossThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        workerThread1.start();
        workerThread2.start();
        System.out.println("server start...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class NettyThread extends Thread {
        private Selector selector;
        private boolean boss;
        private int id;
        private static BlockingQueue<SocketChannel>[] queues;
        private static AtomicInteger count = new AtomicInteger(0);

        // boss 线程构造函数
        public NettyThread(Selector selector, int queueNum) {
            super("BOSS");
            this.selector = selector;
            this.boss = true;
            queues = new BlockingQueue[queueNum];
            for (int i = 0; i < queueNum; i++) {
                queues[i] = new LinkedBlockingQueue<>();
            }
            System.out.println("boss thread start");
        }

        // worker 线程构造函数
        public NettyThread(Selector selector) {
            super("WORKER" + count.incrementAndGet() % queues.length);
            this.selector = selector;
            this.boss = false;
            this.id = count.get() % queues.length;
            System.out.println("worker " + this.id + " thread start");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // boss + worker 都有各自的selector，此处while都会执行
                    // boss 处理acceptable事件
                    // worker 处理 readable事件
                    while (selector.select(100) > 0) {
                        Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
                        while (selectionKeys.hasNext()) {
                            SelectionKey selectionKey = selectionKeys.next();
                            selectionKeys.remove();
                            if (selectionKey.isAcceptable()) {
                                acceptHandler(selectionKey);
                            } else if (selectionKey.isReadable()) {
                                readHandler(selectionKey);
                            } else if (selectionKey.isWritable()) {
                                //writable事件一般只在向channel write返回0表示发送缓冲区满时注册writable事件
                                //并且在下次write发送完成后要取消writable事件，不然每次都会select出writable事件导致其他事件得不到处理
                                System.out.println("Writable event coming: " + selectionKey.channel().toString());
                            }
                        }
                    }
                    // worker从队列取client并注册read事件
                    if (!boss && !queues[id].isEmpty()) {
                        SocketChannel client = queues[id].take();
                        client.configureBlocking(false);
                        ByteBuffer clientBuf = ByteBuffer.allocate(1024);
                        client.register(selector, SelectionKey.OP_READ, clientBuf);
                        System.out.println("---new client---" + client.getRemoteAddress() + " in queue " + id);
                    }

                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void acceptHandler(SelectionKey selectionKey) throws IOException {
            ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel client = serverChannel.accept();
            client.configureBlocking(false);
            queues[count.incrementAndGet() % queues.length].offer(client);
        }

        private void readHandler(SelectionKey selectionKey) {
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
                    } else {
                        // 客户端close会触发read事件，read返回-1表示客户端关闭
                        // -1 close_wait 不加-1判断，客户端主动close会导致空轮询bug
                        System.out.println("client---" + client.getRemoteAddress() + " close");
                        client.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
