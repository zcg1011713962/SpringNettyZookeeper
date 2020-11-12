package netty.server;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

public class SslContextFactory {
    private static final String PROTOCOL = "TLS";
    private static SSLContext SERVER_CONTEXT = null;
    private static SSLContext CLIENT_CONTEXT = null;
    private static Object syncObj = new Object();

    private SslContextFactory(){

    }


    public static SSLContext getServerContext() throws Exception {
        if (SERVER_CONTEXT == null) {
            synchronized(syncObj) {
                if (SERVER_CONTEXT == null) {
                    KeyManagerFactory kmf = null;
                    // 密钥库
                    KeyStore ks = KeyStore.getInstance("JKS");
                    // 加载密钥 密钥仓库密码
                    ks.load(SslContextFactory.class.getClassLoader().getResourceAsStream("nettyServer.jks"), "77777777".toCharArray());
                    kmf = KeyManagerFactory.getInstance("SunX509");
                    // 初始化密钥管理器, keypass 指定别名条目的密码(私钥的密码)
                    kmf.init(ks, "66666666".toCharArray());
                    SERVER_CONTEXT = SSLContext.getInstance(PROTOCOL);
                    SERVER_CONTEXT.init(kmf.getKeyManagers(), null, null);
                }
            }
        }
        return SERVER_CONTEXT;
    }

    public static SSLContext getClientContext() throws Exception {
        if(CLIENT_CONTEXT!=null) return CLIENT_CONTEXT;
        TrustManagerFactory tf = null;
        //密钥库KeyStore
        KeyStore tks = KeyStore.getInstance("JKS");
        //加载客户端证书
        tks.load(SslContextFactory.class.getClassLoader().getResourceAsStream("nettyClient.jks"), "99999999".toCharArray());
        tf = TrustManagerFactory.getInstance("SunX509");
        // 初始化信任库
        tf.init(tks);
        CLIENT_CONTEXT = SSLContext.getInstance(PROTOCOL);
        //设置信任证书
        CLIENT_CONTEXT.init(null, tf == null ? null : tf.getTrustManagers(), null);
        return CLIENT_CONTEXT;
    }


}
