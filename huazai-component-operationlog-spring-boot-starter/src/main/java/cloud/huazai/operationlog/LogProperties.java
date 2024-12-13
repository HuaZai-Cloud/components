package cloud.huazai.operationlog;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * LogProperties
 *
 * @author devon
 * @since 2024/12/13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "logging")
public class LogProperties {

    /**
     * 日志文件路径
     */
    private String file;

    /**
     * 日志级别
     */
    private String level = "INFO";


}
