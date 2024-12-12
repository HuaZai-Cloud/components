package cloud.huazai.datatransmission.request;

import cloud.huazai.datatransmission.request.Query;
import cloud.huazai.tool.java.lang.StringUtils;
import lombok.Getter;

/**
 * PageQuery
 *
 * @author devon
 * @since 2024/12/12
 */
@Getter
public class PageQuery extends Query {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private int pageSize = DEFAULT_PAGE_SIZE;

    private int pageIndex = 1;

    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }else{
            this.pageSize = pageSize;
        }
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = Math.max(pageIndex, 1);
    }



}
