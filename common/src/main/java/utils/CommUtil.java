package utils;

import execptions.ClientStartException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import netty.server.packet.Packet;
import org.apache.commons.lang3.StringUtils;

import java.util.StringJoiner;

public class CommUtil {
    public static String getZookeeperServerList(){
        StringJoiner join = new StringJoiner(",");
        join.add( "192.168.117.200:2181");
        join.add( "192.168.117.200:2182");
        join.add( "192.168.117.200:2183");
        return join.toString();
    }

    /**
     *
     * @param host
     * @param port
     * @return url
     * @throws ClientStartException
     */
    public static String getUrl(String host, int port) throws ClientStartException {
        if(StringUtils.isBlank(host) || port == -1){
            throw new ClientStartException("指定主机或端口错误");
        }
        return "wss://" + host + ":" + port + "/";
    }

}
