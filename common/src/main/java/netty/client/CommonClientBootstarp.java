package netty.client;

import execptions.ClientStartException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import jdk.nashorn.internal.ir.ReturnNode;
import netty.client.Interface.IClientRun;
import org.apache.commons.lang3.StringUtils;
import utils.CommUtil;
import utils.LogUtil;
import zookeeper.ZookeeperClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CommonClientBootstarp extends WebSocketClient implements IClientRun {
    public CommonClientBootstarp(){
        System.setProperty("javax.net.debug", "all");
    }

    @Override
    public void connect(String host, int port){
        URI websocketURI = null;
        try {
            websocketURI = new URI(getUrl(host, port));
        } catch (URISyntaxException | ClientStartException e) {
            LogUtil.error("构造websocketuri",e);
        }
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        // 进行握手
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, null, true,httpHeaders);
        try {
            final Channel channel = getConnection().connect(websocketURI.getHost(),websocketURI.getPort()).sync().channel();
            WebSocketClientHandler handler = (WebSocketClientHandler)channel.pipeline().get("hookedHandler");
            handler.setHandshaker(handshaker);
            handshaker.handshake(channel);
            //阻塞等待是否握手成功
            ChannelPromise h = handler.getHandshakeFuture().sync();
        } catch (InterruptedException e) {
            LogUtil.error("websocket connect sync",e);
        }

    }

    @Override
    public List<String> connectZookeeper(String nodeName) {
        return acquireServer(nodeName);
    }

    /**
     * 获取服务器列表
     * @param nodeName 节点
     */
    private List<String> acquireServer(String nodeName){
        List<String> list = null;
        try {
            ZookeeperClient zookeeperClient = new ZookeeperClient(CommUtil.getZookeeperServerList(),5000,5000);
            list = zookeeperClient.getNode(nodeName);
        } catch (Exception e) {
            LogUtil.error("nettyClient连接zookeeper", e);
        }
        return list == null ? new ArrayList<>() : list;
    }

    /**
     *
     * @param host
     * @param port
     * @return url
     * @throws ClientStartException
     */
    private String getUrl(String host, int port) throws ClientStartException {
        if(StringUtils.isBlank(host) || port == -1){
            throw new ClientStartException("指定主机或端口错误");
        }
        return "wss://" + host + ":" + port + "/";
    }
}
