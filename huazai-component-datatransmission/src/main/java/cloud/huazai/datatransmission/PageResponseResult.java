package cloud.huazai.datatransmission;

import cloud.huazai.tool.java.util.CollectionUtils;
import lombok.Getter;
import lombok.Setter;
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
public class PageResponseResult<T> extends ResponseResult{

    private int totalCount = 0;

    private int pageSize = 1;

    private int pageIndex = 1;

    private Collection<T> data;

    public void setTotalCount(int totalCount) {
        this.totalCount = Math.max(totalCount, 0);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = Math.max(pageSize, 1);
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = Math.max(pageIndex, 1);
    }

    public void setData(Collection<T> collData) {
        this.data = CollectionUtils.isEmpty(collData) ? new ArrayList<>():collData;
    }
}
