package message.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import message.Message;

/**
 *
 */
public class MessageClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String value = "TLV-V: 值域";
        Message message = new Message(0x1, value.getBytes().length, value);
        ctx.writeAndFlush(message);
    }
}
