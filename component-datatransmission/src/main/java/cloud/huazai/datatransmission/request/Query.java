package cloud.huazai.datatransmission.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * Query
 *
 * @author devon
 * @since 2024/12/12
 */
@Getter
public  class Query extends Request {

    @Schema(description = "排序字段")
    private List<SortField> sortFieldList;

}
