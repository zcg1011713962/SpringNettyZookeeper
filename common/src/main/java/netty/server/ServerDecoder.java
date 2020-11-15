package netty.server;

import execptions.PacketFormatException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import netty.server.packet.Packet;

public class ServerDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            ByteBuf in = null;
            try {
                in = ((BinaryWebSocketFrame)msg).content();
                if (in.readableBytes() < 5) {
                    throw new PacketFormatException("长度不足5");
                }
                // 一字节--填充
                if (in.readByte() != 124) {
                    throw new PacketFormatException("长度不足124");
                }
                // 两字节--包长
                short length = in.readShort();
                if (length < 0 || length > 20480) {
                    throw new PacketFormatException("包长大于20480或者小于0");
                }
                // 两字节--指令
                short cmd = in.readShort();
                if (in.readableBytes() < length) {
                    throw new PacketFormatException();
                }
                // 数据
                byte[] bytes = new byte[length];
                in.readBytes(bytes);
                ctx.fireChannelRead(new Packet(cmd, bytes));
            } finally {
                if (in != null) {
                    // 释放buf
                    in.release();
                }

            }

        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
