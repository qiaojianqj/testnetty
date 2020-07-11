package timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import writechannel.WriteChannel;

import java.text.SimpleDateFormat;

/**
 * @description:
 * @create: 2018-11-07 16:17
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        //final ByteBuf time = ctx.alloc ().buffer ();
        //time.writeInt ( (int)(System.currentTimeMillis () / 1000L + 2208988800L) );
        //final ChannelFuture future = ctx.writeAndFlush ( time );
        System.out.println ( "Thread: " + Thread.currentThread ().getName () + ";TimeServer channelActive" );
        WriteChannel.getInstance ().addChannelToCms ( ctx.channel () );
        //final ChannelFuture future = ctx.writeAndFlush ( new UnixTime (  ));
        //future.addListener ( ChannelFutureListener.CLOSE );
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //UnixTime m = (UnixTime) msg;
        ByteBuf m = (ByteBuf)msg;
        System.out.println ("Thread: " + Thread.currentThread ().getName () + ";TimeServer channel " + ctx.channel () + " read: " + new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format(m.readLong ()) );
        //ctx.close ();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
