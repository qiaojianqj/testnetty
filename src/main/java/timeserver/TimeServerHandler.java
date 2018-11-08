package timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import time.UnixTime;

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
        final ChannelFuture future = ctx.writeAndFlush ( new UnixTime (  ));
        future.addListener ( ChannelFutureListener.CLOSE );
    }
}
