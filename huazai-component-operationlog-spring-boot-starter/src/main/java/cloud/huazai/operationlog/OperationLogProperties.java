package cloud.huazai.operationlog;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OperationLogProperties
 *
 * @author devon
 * @since 2024/12/12
 */
@Getter
@Component
public class OperationLogProperties {

    private List<String> include;

    public void setInclude(List<String> include) {
        this.include = include;
    }
}
