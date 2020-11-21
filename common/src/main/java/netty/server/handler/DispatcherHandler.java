package netty.server.handler;


import netty.server.Interface.abs.Handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * handler分发器
 */
public class DispatcherHandler extends Handler {
    private static Map<Integer, Handler> handlerObjectMap = new ConcurrentHashMap();

    public DispatcherHandler(int cmd) {
        registerHandler(cmd, this);
    }

    public static Handler getHandler(int cmd){
        Handler handler = handlerObjectMap.get(cmd);
        if(handler == null){
            throw new NullPointerException("根据cmd:"+cmd+"找不到handler");
        }
        return handlerObjectMap.get(cmd);
    }

    @Override
    public void registerHandler(int cmd, Handler handler) {
        handlerObjectMap.putIfAbsent(cmd, handler);
    }
}
