package message.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import message.Message;

/**
 *
 */
public class MessageClientEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        if (message == null) {
            return;
        }
        String value = (String)message.getValue();
        byte[] valueBytes = value.getBytes();
        byteBuf.writeInt(message.getType());
        byteBuf.writeInt(message.getLength());
        byteBuf.writeBytes(valueBytes);
    }
}
