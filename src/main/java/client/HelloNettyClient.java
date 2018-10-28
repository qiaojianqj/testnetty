package client;

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
            BufferedReader reader = new BufferedReader ( new InputStreamReader ( System.in ) );
            for (;;) {
                String msg = reader.readLine ();
                if (msg == null) {
                    continue;
                }
                channel.writeAndFlush ( msg + "\r\n" );
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public static void main(String... args) {
        HelloNettyClient helloNettyClient = new HelloNettyClient ( 7531, "127.0.0.1" );
        helloNettyClient.start ();
    }
}
