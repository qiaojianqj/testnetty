package message.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import message.Message;

import java.nio.ByteOrder;
import java.util.List;

/**
 *
 */
public class MessageServerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf != null) {
            int type = byteBuf.readInt();
            int length = byteBuf.readInt();
            ByteBuf valueBuf = byteBuf.readBytes(length);
            byte[] valueBytes = new byte[length];
            valueBuf.readBytes(valueBytes);
            String value = new String(valueBytes, "utf-8");
            Message message = new Message(type, length, value);
            list.add(message);
        }

    }
}
