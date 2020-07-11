package timeclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import time.UnixTime;

import java.util.List;

/**
 * @description:
 * @create: 2018-11-07 16:42
 */
public class TimeDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //list.add ( byteBuf.readBytes ( 4 ) );
        if (byteBuf.readableBytes () < 4) {
            return;
        }
        list.add ( new UnixTime ( byteBuf.readUnsignedInt () ) );
    }
}
