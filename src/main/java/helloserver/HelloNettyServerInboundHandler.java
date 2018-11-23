package helloserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import writechannel.WriteChannel;

/**
 * @description: this is helloserver handler
 * @author: Qiao.Jian
 * @create: 2018-07-31 16:41
 */
public class HelloNettyServerInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        WriteChannel.getInstance ().addChannelToCms ( ctx.channel () );
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf)msg;
        byte[] r = new byte[m.readableBytes ()];
        m.readBytes ( r );
        String str = new String ( r );

        System.out.println ("Server Inbound " + ctx.channel () + "==> helloserver: " + str );

        //以下write操作会发送到Outbound处理
        //ByteBuf out = Unpooled.copyLong ( 456l );
        //ctx.write ( out );
        //ctx.flush ();
    }
}
