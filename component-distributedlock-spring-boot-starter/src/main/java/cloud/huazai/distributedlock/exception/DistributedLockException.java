package cloud.huazai.distributedlock.exception;

import cloud.huazai.exception.BaseException;

/**
 * RedisLockException
 *
 * @author devon
 * @since 2024/12/23
 */

public class DistributedLockException extends BaseException {


    private static final String DEFAULT_ERR_CODE = "REDIS_LOCK_ERROR";
    public DistributedLockException(String errMessage) {
        super(DEFAULT_ERR_CODE,errMessage);
    }

    public DistributedLockException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public DistributedLockException(String errMessage, Throwable cause) {
        super(DEFAULT_ERR_CODE,errMessage, cause);
    }

    public DistributedLockException(String errCode, String errMessage, Throwable cause) {
        super(errCode, errMessage, cause);
    }
}
