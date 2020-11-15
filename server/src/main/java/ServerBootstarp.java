import config.ComponentScanConfig;
import netty.server.CommonServerBootstarp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import utils.LogUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerBootstarp extends CommonServerBootstarp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext application = new AnnotationConfigApplicationContext(ComponentScanConfig.class);
        String[] names = application.getBeanDefinitionNames();
        for (String name : names) {
            LogUtil.info("bean注入:{}", name);
        }

        try {
            ServerBootstarp serverBootstarp = new ServerBootstarp();
            String address = InetAddress.getLocalHost().getHostAddress();
            serverBootstarp.connect("/netty/serverA",address, 18090);
        } catch (UnknownHostException e) {
            LogUtil.error("获取主机",e);
        }
    }
}
