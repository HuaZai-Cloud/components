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
@ConfigurationProperties(prefix = "dataaccesslayer.encrypt")
public class EncryptProperties {

    private String secretKey = "default32bitSecretKeyForAES256GCM"; // 增加默认密钥长度

    private String algorithm = "AES-256-GCM"; // 使用更安全的算法

    private String hmacSecretKey = "defaultHMACSecretKey"; // 用于HMAC校验的密钥
}
