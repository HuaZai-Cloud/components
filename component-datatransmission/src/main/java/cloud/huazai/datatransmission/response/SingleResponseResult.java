package cloud.huazai.datatransmission.response;

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
public class SingleResponseResult<T> extends ResponseResult<T> {

    private T data;

    private SingleResponseResult() {}

    public static <T> SingleResponseResult<T> createResponseResult(){
        return new SingleResponseResult<>();
    }

}
