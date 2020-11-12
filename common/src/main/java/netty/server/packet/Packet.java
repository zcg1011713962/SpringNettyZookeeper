package netty.server.packet;

import execptions.PacketFormatException;
import netty.server.connect.Connection;

public class Packet {
    public static final byte HEAD = 124;
    private final byte[] bytes;
    private final short cmd;
    private Connection connection;

    public Packet(short cmd, byte[] bytes) throws PacketFormatException {
        if (bytes == null) {
            throw new PacketFormatException("bytes不允许为空！！");
        } else {
            this.bytes = bytes;
            this.cmd = cmd;
        }
    }

    public short getCmd() {
        return this.cmd;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
