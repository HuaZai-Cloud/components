package cloud.huazai.datatransmission.request;

import cloud.huazai.tool.java.lang.StringUtils;
import lombok.Getter;

/**
 * Query
 *
 * @author devon
 * @since 2024/12/12
 */
@Getter
public  class Query extends Request {

    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    private String orderBy;

    private String orderDirection = DESC;

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
