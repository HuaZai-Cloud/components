package cloud.huazai.operationlog;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OperationLogProperties
 *
 * @author devon
 * @since 2024/12/12
 */
@Component
public class OperationLogProperties {

    private List<String> include;

    public List<String> getInclude() {
        return include;
    }

    public void setInclude(List<String> include) {
        this.include = include;
    }
}
