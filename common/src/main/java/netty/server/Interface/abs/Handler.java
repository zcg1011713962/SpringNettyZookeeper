package netty.server.Interface.abs;

import netty.server.packet.Packet;

public abstract class Handler {
    /**
     * 注册handler
     * @param cmd
     * @param handler
     */
    public abstract void registerHandler(int cmd, Handler handler);

    /**
     * 执行方法
     * @param packet
     */
    public void exec(Packet packet){}
}
