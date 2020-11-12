package netty.server.Interface;

import java.security.PrivateKey;

public interface IServerRun {
    /**
     * 读取配置
     */
    void getSystemEnv();

    /**
     * 运行
     */
    void run(int port);

    /**
     * 连接zookeeper服务
     * @param nodeName 节点名称
     * @param host 存储数据
     */
    void connectZookeeper(String nodeName, String host, int port);

}
