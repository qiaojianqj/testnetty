package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: this is a client handler
 * @author: Qiao.Jian
 * @create: 2018-07-31 16:17
 */
public class HelloNettyClientHandler extends ChannelInboundHandlerAdapter{

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
       System.out.println ( "server say: " + var2.toString () );
   }
}
