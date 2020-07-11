package message.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 *
 */
public class MessageClient {
    private String address;
    private int port;
    public MessageClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        NioEventLoopGroup acceptGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(acceptGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>(

                ) {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new MessageClientEncoder());
                       socketChannel.pipeline().addLast(new MessageClientHandler());
                    }
                });
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(address, port).sync();
            System.out.println("message client connecting...");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            acceptGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        new MessageClient("localhost", 1234).start();
    }
}
