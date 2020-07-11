package timeclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import writechannel.WriteChannel;

import java.text.SimpleDateFormat;

/**
 * @description:
 * @create: 2018-11-07 16:36
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println ( "Thread: " + Thread.currentThread ().getName () + ";TimeClient channelActive" );
        WriteChannel.getInstance ().addChannelToCms ( ctx.channel () );
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ByteBuf m = (ByteBuf)msg;
        //try {
        //    long currentTimeMillis = (m.readUnsignedInt () - 2208988800L ) * 1000L;
        //    System.out.println ( new Date ( currentTimeMillis ) );
        //    ctx.close ();
        //} finally {
        //    m.release ();
        //}
        //UnixTime m = (UnixTime) msg;
        ByteBuf m = (ByteBuf)msg;
        System.out.println ("Thread: " + Thread.currentThread ().getName () + ";TimeClient channel " + ctx.channel () +  " read: " + new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format (m.readLong ()));
        //ctx.close ();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
