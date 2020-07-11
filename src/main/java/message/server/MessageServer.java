package message.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *
 */
public class MessageServer {
    private int port;
    public MessageServer(int port) {
        this.port = port;
    }

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group( bossGroup, workGroup )
                .channel( NioServerSocketChannel.class )
                .option( ChannelOption.SO_BACKLOG, 512 )
                .childHandler( new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        // add Decoder
                        channel.pipeline().addLast(
                            new MessageServerDecoder()
                        );
                        // add handler
                        channel.pipeline().addLast(
                            new MessageServerHandler()
                        );
                    }
                } );
        //try {
            ChannelFuture future = bootstrap.bind(port);
            System.out.println("message server starting...");
            //future.channel().closeFuture().sync();
            future.channel().closeFuture().addListener(
                    (ChannelFuture channelFuture) -> {
                        System.out.println("Channel close");
                        channelFuture.channel().close();
                        bossGroup.shutdownGracefully();
                        workGroup.shutdownGracefully();
                    }
            );
        //} catch (InterruptedException e) {
        //    bossGroup.shutdownGracefully();
        //    workGroup.shutdownGracefully();
        //}
        System.out.println("Main Thread Over");
    }

    public static void main(String[] args) {
        new MessageServer(1234).start();

    }


}
