package cloud.huazai.exception;





import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;

import java.util.Collection;

/**
 * Assert
 *
 * @author devon
 * @since 2024/12/10
 */

public class Assert {


    // ---------true-----------------------
    public static void isTrue(boolean expression, String errCode, String errMessage) {
        if (!expression) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isTrue(boolean expression, String errMessage) {
        if (!expression) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new BusinessException("Must be true");
        }
    }


    // ------false--------------------
    public static void isFalse(boolean expression, String errCode, String errMessage) {
        if (expression) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isFalse(boolean expression, String errMessage) {
        if (expression) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isFalse(boolean expression) {
        if (expression) {
            throw new BusinessException("Must be false");
        }

    }

    // String
    public static void isNotBlank(CharSequence cs,String errCode, String errMessage) {

        if (StringUtils.isBlank(cs)) {
            throw new BusinessException(errCode, errMessage);
        }

    }

    public static void isNotBlank(CharSequence cs,String errMessage) {

        if (StringUtils.isBlank(cs)) {
            throw new BusinessException(errMessage);
        }

    }

    public static void isNotBlank(CharSequence cs) {

        if (StringUtils.isBlank(cs)) {
            throw new BusinessException("Must be notBlank");
        }

    }

    public static void isBlank(CharSequence cs,String errCode, String errMessage) {

        if (StringUtils.isNotBlank(cs)) {
            throw new BusinessException(errCode, errMessage);
        }

    }

    public static void isBlank(CharSequence cs,String errMessage) {

        if (StringUtils.isNotBlank(cs)) {
            throw new BusinessException(errMessage);
        }

    }

    public static void isBlank(CharSequence cs) {

        if (StringUtils.isNotBlank(cs)) {
            throw new BusinessException("Must be blank");
        }

    }

    // ------Object----------------
    public static void isNull(Object object,String errCode, String errMessage){
        if (object != null) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isNull(Object object,String errMessage) {

        if (object != null) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isNull(Object object) {

        if (object != null) {
            throw new BusinessException("Must be null");
        }
    }

    public static void isNotNull(Object object,String errCode, String errMessage){
        if (object == null) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isNotNull(Object object,String errMessage) {

        if (object == null) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isNotNull(Object object) {

        if (object == null) {
            throw new BusinessException("Must be notNull");
        }
    }


    public static void isEmpty(Collection<?> coll,String errCode, String errMessage) {

        if (CollectionUtils.isNotEmpty(coll)) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isEmpty(Collection<?> coll,String errMessage) {

        if (CollectionUtils.isNotEmpty(coll)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isEmpty(Collection<?> coll) {

        if (CollectionUtils.isNotEmpty(coll)) {
            throw new BusinessException("Must be empty");
        }
    }

    public static void isNotEmpty(Collection<?> coll,String errCode, String errMessage) {

        if (CollectionUtils.isEmpty(coll)) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isNotEmpty(Collection<?> coll,String errMessage) {

        if (CollectionUtils.isEmpty(coll)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isNotEmpty(Collection<?> coll) {

        if (CollectionUtils.isEmpty(coll)) {
            throw new BusinessException("Must be notEmpty");
        }
    }




}
