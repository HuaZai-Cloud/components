package cloud.huazai.datatransmission.response;

import cloud.huazai.tool.java.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MultiResponseResult
 *
 * @author devon
 * @since 2024/12/12
 */

@Getter
@ToString
public class PageResponseResult<T> extends ResponseResult<T> {

    private long totalCount = 0;

    private long pageSize = 1;

    private long pageIndex = 1;

    private Collection<T> data;

    public void setTotalCount(long totalCount) {
        this.totalCount = Math.max(totalCount, 0);
    }

    public void setPageSize(long pageSize) {
        this.pageSize = Math.max(pageSize, 1);
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = Math.max(pageIndex, 1);
    }

    public void setData(Collection<T> collData) {
        this.data = CollectionUtils.isEmpty(collData) ? new ArrayList<>():collData;
    }

    private PageResponseResult() {}

    public static <T> PageResponseResult<T> createResponseResult(){
        return new PageResponseResult<>();
    }
}
