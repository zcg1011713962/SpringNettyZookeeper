import config.ComponentScanConfig;
import context.ApplicationContextProvider;
import netty.client.CommonClientBootstarp;
import netty.server.packet.Packet;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import utils.LogUtil;

import java.util.List;

public class ClientBootstrap extends CommonClientBootstarp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext application = new AnnotationConfigApplicationContext(ComponentScanConfig.class);
        String[] names = application.getBeanDefinitionNames();
        for (String name : names) {
            LogUtil.info("bean注入:{}", name);
        }

        CommonClientBootstarp clientBootstrap = new ClientBootstrap();
        // 从zookeeper服务拉取netty服务列表
        List<String> serverList = clientBootstrap.connectZookeeper("/netty");
        if (serverList.size() > 0) {
            String address = serverList.get(0);
            clientBootstrap.connectNetty(address.split(":")[0], Integer.parseInt(address.split(":")[1]));

            Packet packet = new Packet((short) 110, "这是protobuf数据".getBytes());
            clientBootstrap.write(packet);
        }
    }
    }
