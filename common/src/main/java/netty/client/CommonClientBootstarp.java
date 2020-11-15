package netty.client;

import netty.client.Interface.IClientRun;
import utils.CommUtil;
import utils.LogUtil;
import zookeeper.ZookeeperClient;

import java.util.ArrayList;
import java.util.List;

public class CommonClientBootstarp extends WebSocketClient implements IClientRun {
    public CommonClientBootstarp(){
        // 打印https握手过程
        // System.setProperty("javax.net.debug", "all");
    }

    @Override
    public void connectNetty(String host, int port) {
        super.connect(host, port);
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

}
