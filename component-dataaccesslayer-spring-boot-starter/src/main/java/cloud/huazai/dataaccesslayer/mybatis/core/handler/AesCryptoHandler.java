package cloud.huazai.dataaccesslayer.mybatis.core.handler;

import cloud.huazai.dataaccesslayer.mybatis.config.EncryptProperties;
import cloud.huazai.dataaccesslayer.mybatis.core.crypto.CryptoHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/**
 * AesCryptoService
 *
 * @author Devon
 * @since 2025/8/1 11:59
 */
@Component
@ConditionalOnMissingBean(CryptoHandler.class)
public class AesCryptoHandler implements CryptoHandler {
    private final SecretKey key;

    public AesCryptoHandler(EncryptProperties properties) throws Exception {
        String keyStr = properties.getSecretKey();
        if (keyStr == null || keyStr.trim().isEmpty()) {
            throw new IllegalArgumentException("encrypt.aes.secret-key 不能为空");
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedKey = digest.digest(keyStr.getBytes(StandardCharsets.UTF_8));

        // 根据配置选择合适的密钥长度
        int keyLength;
        switch (properties.getAlgorithm()) { // 假设 EncryptProperties 中有一个 getAlgorithm 方法
            case "AES-192":
                keyLength = 24; // 对应于 AES-192
                break;
            case "AES-256":
                keyLength = 32; // 对应于 AES-256
                break;
            default: // 默认情况下使用 AES-128
                keyLength = 16; // 对应于 AES-128
        }

        byte[] keyBytes = Arrays.copyOf(hashedKey, keyLength);
        this.key = new SecretKeySpec(keyBytes, "AES");
    }

    @Override
    public String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    @Override
    public String decrypt(String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, "UTF-8");
    }
}
