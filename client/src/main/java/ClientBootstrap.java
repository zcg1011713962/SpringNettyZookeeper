import config.ComponentScanConfig;
import netty.client.CommonClientBootstarp;
import netty.server.packet.Packet;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import rpc.protobuf.LoginRequestModel;
import utils.LogUtil;

import java.util.List;

@ComponentScan(basePackages = "rpc.client.handler")
@Configuration
public class ClientBootstrap extends CommonClientBootstarp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext application = new AnnotationConfigApplicationContext(ComponentScanConfig.class, ClientBootstrap.class);
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

            LoginRequestModel.LoginRequest loginRequest = loginRequest("小张","小张的密码");
            Packet packet = new Packet((short) 110, loginRequest.toByteArray());
            clientBootstrap.write(packet);
        }
    }

    /**
     * 登录请求
     * @param userName
     * @param password
     * @return
     */
    public static LoginRequestModel.LoginRequest loginRequest(String userName, String password) {
        // 构造一个protobuf数据
        LoginRequestModel.LoginRequest.Builder builder = LoginRequestModel.LoginRequest.newBuilder();
        builder.setUserName(userName);
        builder.setPassWord(password);

        LoginRequestModel.LoginRequest loginRequest = builder.build();
        return loginRequest;
    }

}
