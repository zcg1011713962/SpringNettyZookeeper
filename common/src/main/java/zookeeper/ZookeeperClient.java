package zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import utils.LogUtil;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class ZookeeperClient {
    private CuratorFramework client;

    public ZookeeperClient(String connectString, int sessionTimeoutMs, int connectionTimeoutMs) throws Exception {
       start(connectString, sessionTimeoutMs, connectionTimeoutMs);
    }

    /**
     * 开始连接zookeeper服务器
     * @param connectString 连接服务
     * @param sessionTimeoutMs 会话超时
     * @param connectionTimeoutMs 连接超时
     */
    public void start(String connectString, int sessionTimeoutMs, int connectionTimeoutMs){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    /**
     *递归创建节点
     * @param path 目录
     * @param data 内容
     * @param mode 创建类型 Curator默认创建的是持久节点
     * @return
     * @throws Exception
     */
    public String createNode(String path, String data, CreateMode mode) throws Exception {
        return client.create().creatingParentContainersIfNeeded()
                .withMode(mode)
                .forPath(path, data.getBytes());
    }

    /**
     * 递归删除节点
     * @param path 路径
     * @throws Exception
     */
    public void deleteNode(String path) throws Exception {
        client.delete().forPath(path);
    }

    /**
     * 存在节点
     * @param path
     * @throws Exception
     * @return
     */
    public Stat exists(String path) throws Exception {
        return client.checkExists().forPath(path);
    }

    /**
     *获取节点数据
     * @return
     * @throws Exception
     */
    public String getData(String path) throws Exception {
       byte[] b= client.getData().forPath(path);
       if(b !=null && b.length > 0){
           String str= new String(b,"utf8");
           return str;
       }
       return null;
    }

    public List<String> getNode(String path) throws Exception {
        List<String> list = client.getChildren().forPath(path);
        ArrayList<String> datas = new ArrayList<>();
        for (String str: list) {
            String data= getData(path+ "/" + str);
            if(!StringUtils.isBlank(data)){
                datas.add(data);
                LogUtil.info("节点:{}获取活跃--服务器：{}", str, data);
            }
        }
        return datas;
    }

}
