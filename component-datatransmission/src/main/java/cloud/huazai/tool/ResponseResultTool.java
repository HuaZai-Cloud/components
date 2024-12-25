package cloud.huazai.tool;

import cloud.huazai.datatransmission.response.MultiResponseResult;
import cloud.huazai.datatransmission.response.PageResponseResult;
import cloud.huazai.datatransmission.response.ResponseResult;
import cloud.huazai.datatransmission.response.SingleResponseResult;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ResponseResultTool
 *
 * @author devon
 * @since 2024/12/12
 */

public class ResponseResultTool {

    private static final String SUCCESS_CODE ="200";

    public static ResponseResult buildSuccess() {
        ResponseResult response = new ResponseResult();
        response.setSuccess(true);
        response.setCode(SUCCESS_CODE);
        return response;
    }

    public static ResponseResult buildFailure(String errCode, String errMessage) {
        ResponseResult response = new ResponseResult();
        response.setSuccess(false);
        response.setCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static <T> SingleResponseResult<T> buildSuccess(T data) {
        SingleResponseResult<T> response = new SingleResponseResult<>();
        response.setSuccess(true);
        response.setCode(SUCCESS_CODE);
        response.setData(data);
        return response;
    }


    public static <T> MultiResponseResult<T> buildSuccess(Collection<T> collData) {
        MultiResponseResult<T> response = new MultiResponseResult<>();
        response.setSuccess(true);
        response.setCode(SUCCESS_CODE);
        response.setData(collData);
        return response;
    }

    public static <T> PageResponseResult<T> buildSuccess(Collection<T> collData, int totalCount, int pageSize, int pageIndex) {
        PageResponseResult<T> response = new PageResponseResult<>();
        response.setSuccess(true);
        response.setCode(SUCCESS_CODE);
        response.setData(collData);
        response.setTotalCount(totalCount);
        response.setPageSize(pageSize);
        response.setPageIndex(pageIndex);
        return response;
    }

    public static <T> PageResponseResult<T> buildSuccess(int pageSize, int pageIndex) {
        PageResponseResult<T> response = new PageResponseResult<>();
        response.setSuccess(true);
        response.setCode(SUCCESS_CODE);
        response.setData(new ArrayList<>());
        response.setTotalCount(0);
        response.setPageSize(pageSize);
        response.setPageIndex(pageIndex);
        return response;
    }


}
