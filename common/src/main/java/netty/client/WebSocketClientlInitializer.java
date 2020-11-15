package netty.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslHandler;
import netty.server.SslContextFactory;

import javax.net.ssl.SSLEngine;

public class WebSocketClientlInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        SSLEngine sslEngine = SslContextFactory.getClientContext().createSSLEngine();
        // 客户端模式
        sslEngine.setUseClientMode(true);
        ChannelPipeline p = socketChannel.pipeline();
        p.addFirst(new SslHandler(sslEngine));
        p.addLast(new HttpClientCodec());
        p.addLast(new HttpObjectAggregator(8192));
        p.addLast(WebSocketClientCompressionHandler.INSTANCE);
        p.addLast("hookedHandler",new WebSocketClientHandler());
    }
}
