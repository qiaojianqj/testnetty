package helloserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: this is helloserver handler
 * @author: Qiao.Jian
 * @create: 2018-07-31 16:41
 */
public class HelloNettyServerHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println ( ctx.channel ().remoteAddress () + "==> helloserver: " + msg.toString () );
        ctx.write ( "received msg ok!" );
        ctx.flush ();
    }
}
