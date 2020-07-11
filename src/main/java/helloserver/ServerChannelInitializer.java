package helloserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @description: this is helloclient channel initializer
 * @author: Qiao.Jian
 * @create: 2018-07-31 16:39
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline ();
        //以下顺序很重要: 保证在Inbound的write会传到Outbound
        pipeline.addLast ( "handler2", new HelloNettyServerOutboundHandler ( ) );
        pipeline.addLast ( "handler1", new HelloNettyServerInboundHandler (  ) );
        //不从Inbound里write就不用管顺序
        //pipeline.addLast ( "handler1", new HelloNettyServerInboundHandler (  ) );
        //pipeline.addLast ( "handler2", new HelloNettyServerOutboundHandler ( ) );
    }
}
