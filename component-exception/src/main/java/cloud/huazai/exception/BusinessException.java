package cloud.huazai.exception;

/**
 * BusinessException
 *
 * @author devon
 * @since 2024/12/11
 */

public class BusinessException extends BaseException{


    private static final String DEFAULT_ERR_CODE = "BUSINESS_ERROR";
    public BusinessException(String errMessage) {
        super(DEFAULT_ERR_CODE,errMessage);
    }

    public BusinessException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public BusinessException(String errMessage, Throwable cause) {
        super(DEFAULT_ERR_CODE,errMessage, cause);
    }

    public BusinessException(String errCode, String errMessage, Throwable cause) {
        super(errCode, errMessage, cause);
    }
}
