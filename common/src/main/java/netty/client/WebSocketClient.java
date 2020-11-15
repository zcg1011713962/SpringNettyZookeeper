package netty.client;

import execptions.ClientStartException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import netty.server.connect.Connection;
import netty.server.packet.Packet;
import utils.CommUtil;
import utils.LogUtil;
import utils.PacketUtil;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient{
    protected static EventLoopGroup group = null;
    private Channel channel;

    /**
     * 获取连接
     * @return
     */
    private Bootstrap getBootstrap(){
        Bootstrap bootstrap= new Bootstrap();
        bootstrap.group(getClientGroup())
                 .channel(NioSocketChannel.class)
                 .handler(new WebSocketClientlInitializer());
        return bootstrap;
    }


    /**
     * 客户端可能多次调用
     * @return
     */
    private EventLoopGroup getClientGroup() {
        // 双重校验
        if (group == null) {
            Class var1 = WebSocketClient.class;
            synchronized(WebSocketClient.class) {
                if (group == null) {
                    int threadCount = Runtime.getRuntime().availableProcessors();
                    group = new NioEventLoopGroup(threadCount);
                    LogUtil.info("netty client EventLoopGroup",threadCount);
                }
            }
        }
        return group;
    }

    /**
     * 客户端连接netty服务
     * @param host 主机
     * @param port 端口
     */
    public void connect(String host, int port){
        URI websocketURI = null;
        try {
            websocketURI = new URI(CommUtil.getUrl(host, port));
        } catch (URISyntaxException | ClientStartException e) {
            LogUtil.error("构造websocketuri",e);
        }
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        // 进行握手
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, null, true,httpHeaders);
        try {
            final Channel channel = getBootstrap().connect(websocketURI.getHost(),websocketURI.getPort()).sync().channel();
            WebSocketClientHandler handler = (WebSocketClientHandler)channel.pipeline().get("hookedHandler");
            handler.setHandshaker(handshaker);
            handshaker.handshake(channel);
            //阻塞等待是否握手成功
            ChannelPromise h = handler.getHandshakeFuture().sync();
            if(h.isSuccess()){
                LogUtil.info("--------------------握手成功了");
                this.channel = channel;
            }
        } catch (InterruptedException e) {
            LogUtil.error("websocket connect sync",e);
        }

    }

    /**
     * 给当前管道送数据包
     * @param packet 数据包
     */
    public void write(Packet packet){
        PacketUtil.build(this.channel,packet);
    }

    /**
     * 给指定管道发送数据包
     * @param channel
     * @param packet
     */
    public static void write(Channel channel, Packet packet) {
        PacketUtil.build(channel,packet);
    }
}
