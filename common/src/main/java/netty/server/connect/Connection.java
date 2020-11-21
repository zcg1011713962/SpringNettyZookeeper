package netty.server.connect;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentMap;

public class Connection {
    protected Channel channel;
    private long linkTime;
    // 此连接标识
    private String identifer;

    public Connection(Channel channel){
        this.channel = channel;
    }

    public boolean isConnected() {
        return this.channel == null ? false : this.channel.isActive();
    }

    public void close(){
        if(channel != null){
            channel.close();
        }
    }

    public void setLinkTime(long linkTime){
        this.linkTime = linkTime;
    }

    public long getLinkTime(){
        return linkTime;
    }

    public String getIdentifer() {
        return identifer;
    }

    /**
     * 设置连接标识
     * @param ident
     */
    public void setIdentifer(String ident) {
        ConcurrentMap connConcurrentMap = ConnectionManager.getConnectionManager().connConcurrentMap;
        if(!StringUtils.isBlank(identifer) && connConcurrentMap.get(identifer) != null){
            connConcurrentMap.put(ident, connConcurrentMap.get(identifer));
        }
        this.identifer = ident;
    }
}
