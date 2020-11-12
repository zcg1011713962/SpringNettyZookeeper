import netty.server.CommonServerBootstarp;
import utils.LogUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ServerBootstarp extends CommonServerBootstarp {
    public static void main(String[] args) {
        try {
            ServerBootstarp serverBootstarp = new ServerBootstarp();
            String address = InetAddress.getLocalHost().getHostAddress();
            serverBootstarp.connectZookeeper("/netty/serverA",address, 18090);
        } catch (UnknownHostException e) {
            LogUtil.error("获取主机",e);
        }
    }
}
