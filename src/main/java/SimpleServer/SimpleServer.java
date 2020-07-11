package SimpleServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/*
[qiaojian@Mac QPClient ]% telnet localhost 8090                                                                [1]
        Trying ::1...
        Connected to localhost.
        Escape character is '^]'.
        IamTeacher
        OK,IamSimpleServer first in write3 second in write2 last in write1
        Connection closed by foreign host.
*/
/**
 * @description:
 * @create: 2018-11-24 15:06
 */
public class SimpleServer {

    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup (1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option( ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024)
                .childHandler(new ChannelInitializer<NioSocketChannel> () {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //Inbound read 顺序传递 1 -> 2 -> 3
                        //Outbound write 逆序传递 3 -> 2 -> 1
                        ch.pipeline().addLast(new SimpleDuplex1());
                        ch.pipeline().addLast(new SimpleDuplex2());
                        ch.pipeline().addLast(new SimpleDuplex3());
                    }
                });
        b.bind(8090).sync().channel().closeFuture().sync();
    }
}

class SimpleDuplex1 extends ChannelDuplexHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("---- write 1 ----");
        super.write(ctx, Util.writeBuf ( msg, " last in write1\n" ), promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //read 1 byte
        System.out.println("---- read 1 ----" + Util.readBuf ( msg, 1 ));
        super.channelRead(ctx, msg);
    }
}

class SimpleDuplex2 extends ChannelDuplexHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("---- write 2 ----");
        super.write(ctx, Util.writeBuf ( msg, " second in write2" ), promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //read 2 byte
        System.out.println("---- read 2 ----" + Util.readBuf ( msg, 2 ));
        super.channelRead(ctx, msg);
    }
}

class SimpleDuplex3 extends ChannelDuplexHandler {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("---- write 3 ----");
        super.write(ctx,  Util.writeBuf ( msg, " first in write3" ), promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, final Object msg) throws Exception {
        ByteBuf m = (ByteBuf)msg;
        //read remaining bytes
        System.out.println ("---- read 3 then write OK to Client ----" + Util.readBuf ( msg, m.readableBytes () ));
        ctx.channel().writeAndFlush( ByteBufAllocator.DEFAULT.buffer().writeBytes("OK,IamSimpleServer".getBytes())).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("----- INACTIVE -----");
        super.channelInactive(ctx);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        System.out.println("----- CLOSE -----");
        super.close(ctx, future);
    }
}

class Util {
    static String readBuf(Object msg, int length) {
        ByteBuf m = (ByteBuf)msg;
        byte[] r = new byte[length];
        m.readBytes ( r );
        String str = new String ( r );
        return str;
    }

    static Object writeBuf(Object msg, String str) {
        ByteBuf m0 = (ByteBuf)msg;
        ByteBuf m = Unpooled.buffer (4 * (m0.readableBytes () + str.length ()));
        m.writeBytes (m0);
        m.writeBytes ( str.getBytes () );
        return m;
    }
}