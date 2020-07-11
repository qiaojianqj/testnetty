import java.time.LocalDateTime;

/**
 *
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("start time is " + LocalDateTime.now());
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Test-Daemon-Thread");
        // daemon线程，主线程运行15秒结束后，JVM进程退出
        // 注释掉，默认非daemon线程，主线程运行15秒结束后，非daemon线程Test-Daemon-Thread还在运行，JVM进程不会退出
        t.setDaemon(true);
        t.start();
        Thread.sleep(30*1000);
        System.out.println("end time is " + LocalDateTime.now());
    }
}
