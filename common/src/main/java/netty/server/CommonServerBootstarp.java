package netty.server;

import netty.server.Interface.IServerRun;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import utils.CommUtil;
import utils.LogUtil;
import zookeeper.ZookeeperClient;

import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;


public class CommonServerBootstarp implements IServerRun {
    private int port;
    public CommonServerBootstarp() {
        System.setProperty("javax.net.debug", "all");
        this.getSystemEnv();
    }

    @Override
    public void getSystemEnv() {
        Map<String, String> env = System.getenv();
        LogUtil.info("---------系统环境变量");
        env.entrySet().stream().forEach( e ->
                LogUtil.info("{}={}",e.getKey(),e.getValue())
        );
        LogUtil.info("---------系统环境变量");

        Properties properties = System.getProperties();
        LogUtil.info("---------系统参数");
        properties.entrySet().stream().forEach(p ->
                LogUtil.info("{}={}",p.getKey(),p.getValue())
        );
        LogUtil.info("---------系统参数");
    }

    @Override
    public void run(int port) {
        LogUtil.info("--------启动server{}", port);
        WebSocketServer webSocketServer = new WebSocketServer(port);
        webSocketServer.run(WebSocketServerHandler.class);
    }

    @Override
    public void connectZookeeper(String nodeName, String host, int port) {
        // 启动服务
        this.run(port);
        // 注册服务
        this.registerServer(nodeName, host, port);
    }


    /**
     * 注册服务
     * @param nodeName
     * @param host
     * @param port
     */
    private void registerServer(String nodeName, String host, int port){
        String address = host + ":" +port;
        try {
            ZookeeperClient zookeeperClient = new ZookeeperClient(CommUtil.getZookeeperServerList(),5000,5000);
            Stat stat= zookeeperClient.exists(nodeName);
            if(stat == null){
                LogUtil.info("netty服务器地址:{}",address);
                // 创建临时节点host
                String node = zookeeperClient.createNode(nodeName ,address, CreateMode.EPHEMERAL);
                String str = zookeeperClient.getData(nodeName);
                if(!StringUtils.isBlank(str)){
                    LogUtil.info("成功创建临时节点:{}",node);
                }
            }else{
                LogUtil.info("{}创建临时节点失败"+ address);
            }
        } catch (Exception e) {
            LogUtil.error("nettyServer连接zookeeper", e);
        }
    }


}
