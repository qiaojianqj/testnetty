package helloclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @description:
 * @create: 2018-11-23 14:01
 */
public class HelloNettyClientOutboundHandler extends ChannelOutboundHandlerAdapter {
   @Override
   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
     System.out.println ( "Client Outbound ctx.channel: " + ctx.channel () );
       String m0 = (String)msg;
       ByteBuf m = Unpooled.buffer (4 * m0.length ());
       m.writeBytes ( m0.getBytes () );
       ctx.write(m);
       ctx.flush ();
   }
}
