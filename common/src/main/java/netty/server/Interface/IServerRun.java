package netty.server.Interface;

public interface IServerRun {
    /**
     * 读取配置
     */
    void getSystemEnv();

    /**
     * 连接netty服务
     */
    void connectNetty(int port);

    /**
     * 注册netty服务
     * @param nodeName 节点名称
     * @param host 存储数据
     */
    void registerServer(String nodeName, String host, int port);

}
