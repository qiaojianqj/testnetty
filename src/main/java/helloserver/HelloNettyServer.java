package helloserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: This is a tutorial of netty
 * @author: Qiao.Jian
 * @create: 2018-07-31 16:35
 */
public class HelloNettyServer {
    private int port;

    public HelloNettyServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup ();
        EventLoopGroup workGroup = new NioEventLoopGroup ();
        ServerBootstrap bootstrap = new ServerBootstrap ().group ( bossGroup, workGroup )
                                        .channel ( NioServerSocketChannel.class )
                                        .childHandler ( new ServerChannelInitializer() );
        try {
            ChannelFuture future = bootstrap.bind (port).sync ();
            System.out.println ( "Server bind port returned channel: " + future.channel () );
            future.channel ().closeFuture ().sync ();
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            bossGroup.shutdownGracefully ();
            workGroup.shutdownGracefully ();
        }
    }

    public static void startServer() {
        new Thread (() -> {
            HelloNettyServer nettyServer = new HelloNettyServer ( 7531 );
            nettyServer.start ();
        }).start ();
    }

    public static void main(String... args) {
        HelloNettyServer.startServer ();
    }
}
