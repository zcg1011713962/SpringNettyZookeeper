package netty.server.connect;

import context.ApplicationContextProvider;
import netty.server.WebSocketServerHandler;
import org.apache.commons.lang3.StringUtils;
import netty.server.packet.Packet;
import utils.LogUtil;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 管理所有连接
 */
public final class ConnectionManager{
    public ConcurrentMap<Object, Connection> connConcurrentMap = new ConcurrentHashMap();
    private static ConnectionManager connectionManagerInstance = new ConnectionManager();
    private ConnectionManager(){}

    public static ConnectionManager getConnectionManager(){
        return connectionManagerInstance;
    }


    /**
     * 发送消息
     * @param identifer 连接标识
     * @param packet 数据包
     * @return
     */
    public static boolean sendMessage(String identifer, Packet packet) {
        Connection connection = getConnection(identifer);
        if (connection != null && packet != null) {
            connection.channel.write(packet);
            return true;
        }
        return false;
    }

    /**
     * 连接
     * @param connection
     * @throws Exception
     */
    public static void connected(Connection connection) throws Exception {
        if (connection.getIdentifer() == null) {
            throw new RuntimeException("连接标识为空");
        } else {
            LogUtil.info("有连接建立,标识{}",connection.getIdentifer());
            // 如果存在该连接返回该连接 否则存储该连接
            Connection connOld = (Connection)connectionManagerInstance.connConcurrentMap.putIfAbsent(connection.getIdentifer(), connection);
            // 旧连接未断开
            if (connOld != null && connOld != connection) {
                // 关闭旧连接
                connOld.close();
                // 建立新连接
                connectionManagerInstance.connConcurrentMap.put(connection.getIdentifer(), connection);
            }

        }

        Iterator<IConnListener> iterator = ApplicationContextProvider.getBeansOfType(IConnListener.class).values().iterator();
        while (iterator.hasNext()) {
            try {
                IConnListener iConnListener = (IConnListener) iterator.next();
                iConnListener.connected(connection);
            } catch (Exception e) {
                LogUtil.error("建立连接",e);
            }
        }

    }

    /**
     * 释放连接
     * @param connection
     * @throws Exception
     */
    public static void disConnect(Connection connection) throws Exception {
        // 内存清理连接
        if (connection.getIdentifer() != null) {
            LogUtil.info("有连接断开,标识{}",connection.getIdentifer());
            Connection currConn = (Connection)connectionManagerInstance.connConcurrentMap.get(connection.getIdentifer());
            if (connection == currConn) {
                remove(connection.getIdentifer());
            }
        }

        // 通知实现该接口的类
        Iterator<IConnListener> iterator = ApplicationContextProvider.getBeansOfType(IConnListener.class).values().iterator();
        while (iterator.hasNext()) {
            try {
                IConnListener iConnListener = (IConnListener) iterator.next();
                iConnListener.disConnect(connection);
            } catch (Exception e) {
                LogUtil.error("断开连接",e);
            }
        }
    }

    /**
     * 获取连接
     * @param identifer 连接标识
     * @return
     */
    public static Connection getConnection(String identifer){
        if(!StringUtils.isBlank(identifer)){
            return connectionManagerInstance.connConcurrentMap.get(identifer);
        }
       throw new NullPointerException();
    }

    /**
     * 内存移除
     * @param identifer
     */
    private static void remove(String identifer){
        if(StringUtils.isBlank(identifer)){
            connectionManagerInstance.connConcurrentMap.remove(identifer);
        }
    }

}
