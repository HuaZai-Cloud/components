package cloud.huazai.datatransmission.response;

import cloud.huazai.tool.java.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

/**
 * MultiResponseResult
 *
 * @author devon
 * @since 2024/12/12
 */

@Getter
@ToString
public class CollectionResponseResult<T> extends ResponseResult<T> {

    private Collection<T> data;

    public void setData(Collection<T> collData) {
        this.data = CollectionUtils.isEmpty(collData) ? CollectionUtils.immutableEmptyList() : collData;
    }
}
