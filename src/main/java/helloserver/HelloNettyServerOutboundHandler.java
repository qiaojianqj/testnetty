package helloserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @description:
 * @create: 2018-11-23 14:01
 */
public class HelloNettyServerOutboundHandler extends ChannelOutboundHandlerAdapter{
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println ( "Server Outbound ctx.channel: " + ctx.channel () );
        ByteBuf m = (ByteBuf)msg;

        byte[] r = new byte[m.readableBytes ()];
        m.readBytes ( r );
        String str = new String ( r );
        String outMsg = "added by hellOutBound";

        ByteBuf out = Unpooled.buffer (str.getBytes().length * outMsg.getBytes().length);
        out.writeBytes(str.getBytes());
        out.writeBytes(outMsg.getBytes());

        ctx.write ( out );
        ctx.flush ();
    }
}
