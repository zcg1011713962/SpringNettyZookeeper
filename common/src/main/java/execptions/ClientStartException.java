package execptions;

import org.apache.commons.lang3.StringUtils;

public class ClientStartException extends NullPointerException{
    public ClientStartException(){
    }

    public ClientStartException(String msg){
        if(!StringUtils.isBlank(msg)){
            throw new RuntimeException(msg);
        }
    }
}
