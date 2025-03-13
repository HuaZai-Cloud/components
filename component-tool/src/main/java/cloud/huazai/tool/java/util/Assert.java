package cloud.huazai.tool.java.util;


import cloud.huazai.exception.BusinessException;
import cloud.huazai.tool.java.date.DateUtils;
import cloud.huazai.tool.java.lang.ObjectUtils;
import cloud.huazai.tool.java.lang.StringUtils;

import java.util.Collection;

/**
 * Assert
 *
 * @author devon
 * @since 2024/12/10
 */

public class Assert {


    // ---------true-----------------------

    public static void isTrue(boolean expression) {
        isTrue(expression, "Must be true");
    }

    public static void isTrue(boolean expression, String errMessage) {
        if (!expression) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isTrue(boolean expression, String errCode, String errMessage) {
        if (!expression) {
            throw new BusinessException(errCode, errMessage);
        }
    }


    // ------false--------------------
    public static void isFalse(boolean expression) {
        isFalse(expression, "Must be false");
    }

    public static void isFalse(boolean expression, String errMessage) {
        if (expression) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isFalse(boolean expression, String errCode, String errMessage) {
        if (expression) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    // String
    public static void isNotBlank(CharSequence cs) {
        isNotBlank(cs, "Must not be blank");
    }

    public static void isNotBlank(CharSequence cs,String errMessage) {
        if (StringUtils.isBlank(cs)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isNotBlank(CharSequence cs,String errCode, String errMessage) {
        if (StringUtils.isBlank(cs)) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isBlank(CharSequence cs) {
        isBlank(cs, "Must be blank");
    }

    public static void isBlank(CharSequence cs,String errMessage) {
        if (StringUtils.isNotBlank(cs)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isBlank(CharSequence cs,String errCode, String errMessage) {
        if (StringUtils.isNotBlank(cs)) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    // ------Object----------------
    public static void isNull(Object object) {
        isNull(object, "Must be null");
    }

    public static void isNull(Object object,String errMessage) {
        if (ObjectUtils.isNotNull(object)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isNull(Object object,String errCode, String errMessage){
        if (ObjectUtils.isNotNull(object)) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isNotNull(Object object) {
        isNotNull(object, "Must be notNull");
    }

    public static void isNotNull(Object object,String errMessage) {

        if (ObjectUtils.isNull(object)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isNotNull(Object object,String errCode, String errMessage){
        if (ObjectUtils.isNull(object)) {
            throw new BusinessException(errCode, errMessage);
        }
    }


    public static void isEmpty(Collection<?> coll) {
        isEmpty(coll, "Must be empty");
    }

    public static void isEmpty(Collection<?> coll,String errMessage) {

        if (CollectionUtils.isNotEmpty(coll)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isEmpty(Collection<?> coll,String errCode, String errMessage) {

        if (CollectionUtils.isNotEmpty(coll)) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isNotEmpty(Collection<?> coll) {
        isNotEmpty(coll, "Must be not empty");
    }

    public static void isNotEmpty(Collection<?> coll,String errMessage) {

        if (CollectionUtils.isEmpty(coll)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isNotEmpty(Collection<?> coll,String errCode, String errMessage) {

        if (CollectionUtils.isEmpty(coll)) {
            throw new BusinessException(errCode, errMessage);
        }
    }

    public static void isValidDateTime(String dateTimeStr, String format){
        isValidDateTime(dateTimeStr, format, "Must be dateTime");
    }

    public static void isValidDateTime(String dateTimeStr, String format,String errMessage){
        if (StringUtils.isNotBlank(dateTimeStr) && StringUtils.isNotBlank(format) && !DateUtils.isValidDateTime(dateTimeStr, format)) {
            throw new BusinessException(errMessage);
        }
    }

    public static void isValidDateTime(String dateTimeStr, String format,String errCode,String errMessage){
        if (StringUtils.isNotBlank(dateTimeStr) && StringUtils.isNotBlank(format) && !DateUtils.isValidDateTime(dateTimeStr, format)) {
            throw new BusinessException(errCode,errMessage);
        }
    }




}
