package cloud.huazai.datatransmission.response;

import cloud.huazai.datatransmission.response.ResponseResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SingleResponseResult
 *
 * @author devon
 * @since 2024/12/12
 */

@Getter
@Setter
@ToString
public class SingleResponseResult<T> extends ResponseResult {

    private T data;


}
