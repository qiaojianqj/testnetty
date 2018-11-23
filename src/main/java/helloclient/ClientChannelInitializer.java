package helloclient;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
 * @description: this is helloclient channel initializer
 * @author: Qiao.Jian
 * @create: 2018-07-31 16:00
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast ( "handler1", new HelloNettyClientInboundHandler (  ) );
        pipeline.addLast ( "handler2", new HelloNettyClientOutboundHandler (  ) );
    }
}
