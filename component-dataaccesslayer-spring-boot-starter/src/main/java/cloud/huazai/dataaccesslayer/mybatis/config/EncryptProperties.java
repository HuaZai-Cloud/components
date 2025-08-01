package cloud.huazai.dataaccesslayer.mybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * EncryptProperties
 *
 * @author Devon
 * @since 2025/8/1 12:05
 */
@Data
@ConfigurationProperties(prefix = "crypto.encrypt")
public class EncryptProperties {

    private String secretKey = "default16bitSecretKey";

    private String algorithm  = "AES-256";
}
