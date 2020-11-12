package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import netty.server.packet.Packet;

public class ServerEncoder extends ChannelOutboundHandlerAdapter {
    public ServerEncoder() {
    }

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Packet) {
            Packet packet = (Packet)msg;
            ByteBuf buf = Unpooled.buffer(6 + packet.getBytes().length);
            // 填充数据
            buf.writeByte(124);
            // 包长
            buf.writeShort(packet.getBytes().length);
            // 指令
            buf.writeShort(packet.getCmd());
            // 数据
            buf.writeBytes(packet.getBytes());
            BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buf);
            ctx.writeAndFlush(frame);
        } else {
            ctx.writeAndFlush(msg);
        }
    }
}
