package helloclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import writechannel.WriteChannel;

/**
 * @description: this is a helloclient handler
 * @author: Qiao.Jian
 * @create: 2018-07-31 16:17
 */
public class HelloNettyClientInboundHandler extends ChannelInboundHandlerAdapter{
    //@Override
    //public void channelActive(final ChannelHandlerContext ctx) {
    //    WriteChannel.getInstance ().addChannelToCms ( ctx.channel () );
    //}

    @Override
   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
       ByteBuf m = (ByteBuf)var2;
       //System.out.println ("Client Inbound " + var1.channel () +  "===> helloclient: " + m.readLong () );
        byte[] r = new byte[m.readableBytes ()];
        m.readBytes ( r );
        String str = new String ( r );
        System.out.println ("Client Inbound " + var1.channel () +  "===> helloclient: " + str );
   }
}
