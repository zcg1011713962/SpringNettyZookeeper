package execptions;

import org.apache.commons.lang3.StringUtils;

/**
 * 包异常
 */
public class PacketFormatException extends Exception {
    public PacketFormatException(){

    }

    public PacketFormatException(String msg){
        if(!StringUtils.isBlank(msg)){
            throw new RuntimeException(msg);
        }
    }
}
