package timeclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description:
 * @create: 2018-11-07 16:30
 */
public class TimeClient {
    private int port;
    private String address;

    public TimeClient(int port, String address){
        this.port = port;
        this.address = address;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup ();
        Bootstrap bootstrap = new Bootstrap ();
        bootstrap.group ( group )
                .channel ( NioSocketChannel.class )
                .handler ( new ChannelInitializer<SocketChannel> () {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //此处有顺序，Decoder必须在Handler之前
                       //ch.pipeline ().addLast ( new TimeDecoder (), new TimeClientHandler () );
                        ch.pipeline ().addLast ( new TimeClientHandler () );
                    }
                } );
        try {
            ChannelFuture future  = bootstrap.connect (address, port).sync ();
            future.channel ().closeFuture ().sync ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public static void startClient() {
        new Thread ( () -> {
            TimeClient timeClient = new TimeClient ( 7532, "127.0.0.1" );
            timeClient.start ();
        }).start ();
    }

    public static void main(String... args) {
        TimeClient.startClient ();
    }
}
