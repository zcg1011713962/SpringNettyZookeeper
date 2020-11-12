package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import utils.LogUtil;

public class WebSocketClient{
    protected static EventLoopGroup group = null;

    /**
     * 获取连接
     * @return
     */
    public Bootstrap getConnection(){
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
}
