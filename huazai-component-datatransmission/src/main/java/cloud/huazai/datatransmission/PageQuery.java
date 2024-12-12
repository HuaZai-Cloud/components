package cloud.huazai.datatransmission;

import cloud.huazai.tool.java.lang.StringUtils;
import lombok.Getter;

/**
 * PageQuery
 *
 * @author devon
 * @since 2024/12/12
 */
@Getter
public class PageQuery extends Query{

    private static final int DEFAULT_PAGE_SIZE = 10;

    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    private int pageSize = DEFAULT_PAGE_SIZE;

    private int pageIndex = 1;

    private String orderBy;

    private String orderDirection = DESC;

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

    public void setOrderDirection(String orderDirection) {
        if (ASC.equalsIgnoreCase(orderDirection) || DESC.equalsIgnoreCase(orderDirection)) {
            this.orderDirection = orderDirection;
        }
    }

    public void setOrderBy(String orderBy) {
        if (StringUtils.isNotBlank(orderBy)) {
            this.orderBy = orderBy;
        }
    }


}
