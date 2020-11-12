package netty.client.Interface;

import java.util.List;

public interface IClientRun {
    void connect(String host, int port);

    /**
     * 连接zookeeper服务
     * @param nodeName 节点名
     * @return netty服务器列表
     */
    List<String> connectZookeeper(String nodeName);

}
