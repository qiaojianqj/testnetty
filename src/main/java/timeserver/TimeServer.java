package timeserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import timeclient.TimeDecoder;

/**
 * @description:
 * @create: 2018-11-07 16:25
 */
public class TimeServer {

    private int port;

    public TimeServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup ();
        EventLoopGroup workGroup = new NioEventLoopGroup ();
        EventExecutorGroup busGroup = new DefaultEventExecutorGroup ( 2 );
        ServerBootstrap bootstrap = new ServerBootstrap ().group ( bossGroup, workGroup )
                .channel ( NioServerSocketChannel.class )
                .childHandler ( new ChannelInitializer<SocketChannel>()  {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //此处有顺序，Encoder必须在Handler之前
                        //ch.pipeline ().addLast ( new TimeEncoder (), new TimeServerHandler () );
                        ch.pipeline ().addLast ( new TimeServerHandler () );
                        ch.pipeline ().addLast ( busGroup );
                    }
                }) ;
        try {
            ChannelFuture future = bootstrap.bind (port).sync ();
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
                TimeServer timeServer = new TimeServer ( 7532 );
                timeServer.start ();
        }).start ();
    }

    public static void main(String... args) {
        TimeServer.startServer ();
    }
}
