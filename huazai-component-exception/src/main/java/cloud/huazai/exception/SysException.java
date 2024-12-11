package cloud.huazai.exception;

/**
 * SysException
 *
 * @author devon
 * @since 2024/12/11
 */

public class SysException extends BaseException{

    private static final String DEFAULT_ERR_CODE = "SYS_ERROR";
    public SysException(String errMessage) {
        super(DEFAULT_ERR_CODE,errMessage);
    }

    public SysException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public SysException(String errMessage, Throwable cause) {
        super(DEFAULT_ERR_CODE,errMessage, cause);
    }

    public SysException(String errCode, String errMessage, Throwable cause) {
        super(errCode, errMessage, cause);
    }
}
