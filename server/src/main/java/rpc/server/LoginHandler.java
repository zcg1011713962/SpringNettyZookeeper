package rpc.server;


import com.google.protobuf.InvalidProtocolBufferException;
import netty.server.connect.ConnectionManager;
import netty.server.handler.DispatcherHandler;
import netty.server.packet.Packet;
import org.springframework.stereotype.Component;
import rpc.protobuf.LoginRequestModel;
import rpc.protobuf.LoginResponseModel;
import utils.LogUtil;

@Component
public class LoginHandler extends DispatcherHandler {
    public LoginHandler() {
        super(110);
    }

    @Override
    public void exec(Packet packet) {
        try {
            LoginRequestModel.LoginRequest loginRequest = LoginRequestModel.LoginRequest.parseFrom(packet.getBytes());
            LogUtil.info("{}请求登录,密码{}", loginRequest.getUserName(), loginRequest.getPassWord());
            // 登录成功 把连接给小张
            packet.getConnection().setIdentifer("小张");
            // 给小张发消息
            ConnectionManager.sendMessage("小张", packet("你已经登录成功"));
        } catch (InvalidProtocolBufferException e) {
            LogUtil.error("登录请求字节转换实体", e.getMessage());
        }
    }

    public static Packet packet(String code) {
        LoginResponseModel.LoginResponse.Builder loginResponse = LoginResponseModel.LoginResponse.newBuilder();
        loginResponse.setCode(code);
        Packet p = new Packet((short)666, loginResponse.build().toByteArray());
        return p;
    }

}
