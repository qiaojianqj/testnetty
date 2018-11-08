package timeclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import time.UnixTime;

import java.util.Date;

/**
 * @description:
 * @create: 2018-11-07 16:36
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
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
        UnixTime m = (UnixTime) msg;
        System.out.println ( m );
        ctx.close ();
    }
}
