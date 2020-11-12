package netty.server.connect;

import io.netty.channel.Channel;

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

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }
}
