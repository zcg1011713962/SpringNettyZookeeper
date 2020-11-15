package execptions;

import org.apache.commons.lang3.StringUtils;

public class ChannelException extends NullPointerException{
    public ChannelException(String msg) {
        if(!StringUtils.isBlank(msg)){
            throw new RuntimeException(msg);
        }
    }
}
