package netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import netty.server.connect.Connection;
import netty.server.handler.DispatcherHandler;
import netty.server.packet.Packet;
import utils.LogUtil;
import utils.PacketUtil;

public class WebSocketClientHandler extends SimpleChannelInboundHandler {
    //握手的状态信息
    WebSocketClientHandshaker handshaker;
    //netty自带的异步处理
    ChannelPromise handshakeFuture;

    protected Connection connection;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取协议
        handshakeFuture = ctx.newPromise();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        Channel ch = channelHandlerContext.channel();
        FullHttpResponse response = null;
        if (!this.handshaker.isHandshakeComplete()) {
            // 握手处理
            response = (FullHttpResponse) msg;
            //握手协议返回，设置结束握手
            this.handshaker.finishHandshake(ch, response);
            //设置成功
            this.handshakeFuture.setSuccess();
        }else if (msg instanceof FullHttpResponse) {
            response = (FullHttpResponse)msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }else {
            WebSocketFrame frame = (WebSocketFrame)msg;
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame)frame;
            } else if (frame instanceof BinaryWebSocketFrame) {
                BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame)frame;
                // 拆包
                Packet packet= PacketUtil.unpack(binFrame.content());
                // 分发给handler
                DispatcherHandler.getHandler(packet.getCmd()).exec(packet);
            } else if (frame instanceof PongWebSocketFrame) {
                LogUtil.info("pong");
            } else if (frame instanceof CloseWebSocketFrame) {
                ch.close();
            }
        }

    }


    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelPromise getHandshakeFuture() {
        return handshakeFuture;
    }

}
