package netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    private Class<? extends ChannelHandler> handlerClazz;
    public WebSocketServerInitializer(Class<? extends ChannelHandler> handlerClazz) {
        this.handlerClazz = handlerClazz;
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine sslEngine = SslContextFactory.getServerContext().createSSLEngine();
        // 设置为服务端模式
        sslEngine.setUseClientMode(false);
        // 需要验证客户端
        //sslEngine.setNeedClientAuth(true);
        // 校验需要加到最前面
        pipeline.addFirst(new SslHandler(sslEngine));
        // 依次放到后面
        pipeline.addLast(new ChannelHandler[]{new HttpServerCodec()});
        pipeline.addLast(new ChannelHandler[]{new HttpObjectAggregator(64*1024)});
        pipeline.addLast(new ChannelHandler[]{new WebSocketServerProtocolHandler("/", (String)null, true)});
        // 自定义编解码
        pipeline.addLast(new ChannelHandler[]{new ServerDecoder()});
        pipeline.addLast(new ChannelHandler[]{(ChannelHandler)this.handlerClazz.newInstance()});
        pipeline.addLast(new ChannelHandler[]{new ServerEncoder()});
    }
}
