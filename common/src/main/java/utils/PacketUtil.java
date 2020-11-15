package utils;

import execptions.ChannelException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import netty.server.packet.Packet;

public class PacketUtil {

    /**
     * @param channel 管道
     * @param packet 数据包
     */
    public static void build(Channel channel, Packet packet){
        if(channel == null || !channel.isActive()){
            throw new ChannelException("获取不到管道或管道已关闭");
        }
        ByteBuf buf = Unpooled.buffer(5 + packet.getBytes().length);
        buf.writeByte(124);
        buf.writeShort(packet.getBytes().length);
        buf.writeShort(packet.getCmd());
        buf.writeBytes(packet.getBytes());
        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buf);
        channel.writeAndFlush(frame);
    }
}
