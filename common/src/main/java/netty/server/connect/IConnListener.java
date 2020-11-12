package netty.server.connect;

public interface IConnListener {
    void connected(Connection connection) throws Exception;

    void disConnect(Connection connection) throws Exception;
}
