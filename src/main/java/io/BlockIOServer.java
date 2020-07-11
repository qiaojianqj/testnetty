package io;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 传统IO-同步阻塞IO，一个线程只能处理一个连接
 * @author qiaojian
 */
public class BlockIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(19999);
        System.out.println(Thread.currentThread().getName() + ":new ServerSocket 19999");

        ThreadFactory namedThreadFactory = new DefaultThreadFactory("BIO-TEST-");
        ExecutorService bioThreadPool = new ThreadPoolExecutor(1, 3,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        //bioThreadPool.shutdown();

        while (true) {
            Socket client = serverSocket.accept(); // 阻塞
            System.out.println(Thread.currentThread().getName() + ":rev client: " + client.getInetAddress().toString() + " " + client.getPort() );
            try {
                bioThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (;;) {
                                byte[] rev = new byte[1024];
                                int num = client.getInputStream().read(rev); // 阻塞
                                System.out.println(Thread.currentThread().getName() + ":rev " + new String(rev) + " from " + client.getInetAddress().toString() + " " + client.getPort());
                                if (num > 0) {
                                    client.getOutputStream().write(rev);
                                } else if ( num == 0 ) {
                                    continue;
                                } else {
                                    System.out.println(Thread.currentThread().getName() + ": client: " + client.getInetAddress().toString() + " " + client.getPort() + " close");
                                    client.close();
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (RejectedExecutionException e) {
                System.out.println(Thread.currentThread().getName() + ": RejectedExecutionException, Continue execute");
            }
        }
    }
}
