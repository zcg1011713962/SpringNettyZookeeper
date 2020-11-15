package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import utils.LogUtil;

public class WebSocketServer {
    private Channel channel;

    public void run(Class<? extends ChannelHandler> handlerClazz, int port){
        int threadCount = Runtime.getRuntime().availableProcessors() / 2;
        LogUtil.info("netty server EventLoopGroup",threadCount);

        EventLoopGroup bossGroup = new NioEventLoopGroup(threadCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(threadCount);
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new WebSocketServerInitializer(handlerClazz))
                .option(ChannelOption.SO_BACKLOG, 256)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture channelFuture = b.bind(port).sync();
            this.channel = channelFuture.channel();
        } catch (InterruptedException e) {
           LogUtil.error("--------绑定端口", e);
        }
    }

}
