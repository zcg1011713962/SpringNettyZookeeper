package utils;

import java.util.StringJoiner;

public class CommUtil {
    public static String getZookeeperServerList(){
        StringJoiner join = new StringJoiner(",");
        join.add( "192.168.117.200:2181");
        join.add( "192.168.117.200:2182");
        join.add( "192.168.117.200:2183");
        return join.toString();
    }
}
