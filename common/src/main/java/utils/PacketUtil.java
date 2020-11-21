package utils;

import execptions.ChannelException;
import execptions.PacketFormatException;
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

    /**
     * 拆包
     * @param bytbuf
     * @return
     */
    public static Packet unpack(ByteBuf bytbuf){

        if(bytbuf.readableBytes() < 5){
            throw new PacketFormatException("包长不足5");
        }
        // 一字节--填充
        if (bytbuf.readByte() != 124) {
            throw new PacketFormatException("非约定格式");
        }
        // 两字节--包长
        short length = bytbuf.readShort();
        if (length < 0 || length > 20480) {
            throw new PacketFormatException("包长大于20480或者小于0");
        }
        // 两字节--指令
        short cmd = bytbuf.readShort();
        if (bytbuf.readableBytes() < length) {
            throw new PacketFormatException();
        }
        // 数据
        byte[] bytes = new byte[length];
        bytbuf.readBytes(bytes);
        return new Packet(cmd, bytes);
    }
}
