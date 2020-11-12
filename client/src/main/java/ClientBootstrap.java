import netty.client.CommonClientBootstarp;

import java.util.List;

public class ClientBootstrap extends CommonClientBootstarp {
    public static void main(String[] args) {
        CommonClientBootstarp clientBootstrap = new ClientBootstrap();
        List<String> serverList = clientBootstrap.connectZookeeper("/netty");
        if(serverList.size() > 0){
            String address = serverList.get(0);
            clientBootstrap.connect(address.split(":")[0], Integer.parseInt(address.split(":")[1]));
        }
    }

}
