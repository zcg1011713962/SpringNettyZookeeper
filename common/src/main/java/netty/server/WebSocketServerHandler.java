package netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.server.connect.Connection;
import netty.server.connect.ConnectionManager;
import netty.server.connect.IConnListener;
import netty.server.handler.DispatcherHandler;
import netty.server.packet.Packet;
import utils.LogUtil;

public class WebSocketServerHandler<T extends Packet> extends ChannelInboundHandlerAdapter implements IConnListener{
    protected Connection connection;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.connection = new Connection(ctx.channel());
        // 未登录成功用户默认此标识
        connection.setIdentifer(ctx.channel().toString());
        connection.setLinkTime(System.currentTimeMillis());
        this.connected(this.connection);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.disConnect(this.connection);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Packet){
            Packet packet = (Packet) msg;
            this.connection.setLinkTime(System.currentTimeMillis());
            packet.setConnection(this.connection);
            // 根据cmd 执行不同的handler
            DispatcherHandler.getHandler(packet.getCmd()).exec(packet);
        }else{
            LogUtil.info("-------------------------收到非包消息");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void connected(Connection connection) throws Exception {
        ConnectionManager.connected(connection);
    }

    @Override
    public void disConnect(Connection connection) throws Exception {
        ConnectionManager.disConnect(connection);
    }
}
