package cloud.huazai.tool;

import cloud.huazai.datatransmission.MultiResponseResult;
import cloud.huazai.datatransmission.PageResponseResult;
import cloud.huazai.datatransmission.ResponseResult;
import cloud.huazai.datatransmission.SingleResponseResult;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * ResponseResultToolTest
 *
 * @author devon
 * @since 2024/12/12
 */

public class ResponseResultToolTest {

    @org.junit.Test
    public void buildSuccess() {

        ResponseResult responseResult = ResponseResultTool.buildSuccess();

        System.out.println(responseResult);

    }

    @org.junit.Test
    public void buildFailure() {
        ResponseResult responseResult = ResponseResultTool.buildFailure("404", "接口不存在");
        System.out.println("responseResult = " + responseResult);

    }

    @org.junit.Test
    public void testBuildSuccess() {
        MultiResponseResult<Object> objectMultiResponseResult = ResponseResultTool.buildSuccess(new ArrayList<>());
        System.out.println("objectMultiResponseResult = " + objectMultiResponseResult);
    }

    @org.junit.Test
    public void testBuildSuccess1() {
        SingleResponseResult<String> aaaa = ResponseResultTool.buildSuccess("aaaa");
        System.out.println("aaaa = " + aaaa);
    }

    @org.junit.Test
    public void testBuildSuccess2() {
        PageResponseResult<Object> objectPageResponseResult = ResponseResultTool.buildSuccess(1, 1);
        System.out.println("objectPageResponseResult = " + objectPageResponseResult);
    }

    @org.junit.Test
    public void testBuildSuccess3() {
        PageResponseResult<Object> objectPageResponseResult = ResponseResultTool.buildSuccess(new ArrayList<>(), 1, 1, 1);
        System.out.println("objectPageResponseResult = " + objectPageResponseResult);
    }
}