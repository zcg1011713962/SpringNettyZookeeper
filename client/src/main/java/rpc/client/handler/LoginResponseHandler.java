package rpc.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import netty.server.handler.DispatcherHandler;
import netty.server.packet.Packet;
import org.springframework.stereotype.Component;
import rpc.protobuf.LoginResponseModel;
import utils.LogUtil;

@Component
public class LoginResponseHandler extends DispatcherHandler {
    public LoginResponseHandler() {
        super(666);
    }

    @Override
    public void exec(Packet packet) {
        try {
            LoginResponseModel.LoginResponse loginResponse = LoginResponseModel.LoginResponse.parseFrom(packet.getBytes());
            String code = loginResponse.getCode();
            LogUtil.info("登录响应-----{}", code);
        } catch (InvalidProtocolBufferException e) {
            LogUtil.error("解释登录响应包异常{}", e.getMessage());
        }


    }
}
