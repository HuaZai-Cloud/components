package cloud.huazai.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * BaseException
 *
 * @author devon
 * @since 2024/12/11
 */
@Getter
@Setter
public abstract class BaseException extends RuntimeException{

    private  String errCode;

    public BaseException(String errMessage) {
        super(errMessage);
    }

    public BaseException(String errCode,String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    public BaseException(String errMessage, Throwable cause) {
        super(errMessage, cause);
    }

    public BaseException(String errCode,String errMessage, Throwable cause) {
        super(errMessage, cause);
        this.errCode = errCode;
    }


}
