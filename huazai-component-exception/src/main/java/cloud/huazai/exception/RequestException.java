package cloud.huazai.exception;

/**
 * RequestException
 *
 * @author devon
 * @since 2024/12/11
 */

public class RequestException extends BaseException{

    private static final String DEFAULT_ERR_CODE = "REQUEST_ERROR";
    public RequestException(String errMessage) {
        super(DEFAULT_ERR_CODE,errMessage);
    }

    public RequestException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public RequestException(String errMessage, Throwable cause) {
        super(DEFAULT_ERR_CODE,errMessage, cause);
    }

    public RequestException(String errCode, String errMessage, Throwable cause) {
        super(errCode, errMessage, cause);
    }
}
