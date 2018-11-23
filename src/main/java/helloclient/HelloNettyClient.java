package helloclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @description: This is a tutorial of netty
 * @author: Qiao.Jian
 * @create: 2018-07-31 15:55
 */
public class HelloNettyClient {
    private int port;
    private String address;

    public HelloNettyClient(int port, String address){
       this.port = port;
       this.address = address;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup ();
        Bootstrap bootstrap = new Bootstrap ();
        bootstrap.group ( group )
                .channel ( NioSocketChannel.class )
                .handler ( new ClientChannelInitializer() );
        try {
            Channel channel = bootstrap.connect (address, port).sync ().channel ();
            System.out.println ( "Client connect returned channel: " + channel );
            BufferedReader reader = new BufferedReader ( new InputStreamReader ( System.in ) );
            for (;;) {
                String msg = reader.readLine ();
                System.out.println ( "your input msg: " + msg );
                if (msg == null) {
                    System.out.println ( "error, input again" );
                    continue;
                }
                //以下write操作会发送到Outbound处理
                channel.writeAndFlush (  msg );
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public static void startClient() {
        new Thread ( () -> {
            HelloNettyClient nettyClient = new HelloNettyClient ( 7531, "127.0.0.1" );
            nettyClient.start ();
        }).start ();
    }

    public static void main(String... args) {
        HelloNettyClient.startClient ();
    }
}
