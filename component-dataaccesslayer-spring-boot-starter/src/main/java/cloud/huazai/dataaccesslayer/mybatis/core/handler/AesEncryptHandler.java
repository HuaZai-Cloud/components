package cloud.huazai.dataaccesslayer.mybatis.core.handler;

import cloud.huazai.dataaccesslayer.mybatis.config.EncryptProperties;
import cloud.huazai.dataaccesslayer.mybatis.core.encrypt.EncryptHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * AesCryptoService
 *
 * @author Devon
 * @since 2025/8/1 11:59
 */
@Component
@ConditionalOnMissingBean(EncryptHandler.class)
public class AesEncryptHandler implements EncryptHandler {
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final String AES_ALGORITHM = "AES";

    private final SecretKey key;
    private final String algorithm; // 存储具体算法

    public AesEncryptHandler(EncryptProperties properties) throws Exception {
        String keyStr = properties.getSecretKey();
        if (keyStr == null || keyStr.trim().isEmpty()) {
            throw new IllegalArgumentException("encrypt.aes.secret-key 不能为空");
        }

        this.algorithm = getAlgorithm(properties.getAlgorithm());

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedKey = digest.digest(keyStr.getBytes(StandardCharsets.UTF_8));

        // 根据配置选择合适的密钥长度
        int keyLength = getKeyLength(properties.getAlgorithm());
        byte[] keyBytes = Arrays.copyOf(hashedKey, keyLength);
        this.key = new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }

    @Override
    public String encrypt(String plaintext) throws Exception {
        if ("AES/GCM/NoPadding".equals(algorithm)) {
            return encryptGCM(plaintext);
        } else {
            throw new UnsupportedOperationException("不支持的加密算法: " + algorithm);
        }
    }

    @Override
    public String decrypt(String ciphertext) throws Exception {
        if ("AES/GCM/NoPadding".equals(algorithm)) {
            return decryptGCM(ciphertext);
        } else {
            throw new UnsupportedOperationException("不支持的解密算法: " + algorithm);
        }
    }

    private String encryptGCM(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // 将IV和加密数据组合在一起
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
        byteBuffer.put(iv);
        byteBuffer.put(encrypted);
        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    private String decryptGCM(String ciphertext) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);
        byte[] encrypted = new byte[byteBuffer.remaining()];
        byteBuffer.get(encrypted);

        Cipher cipher = Cipher.getInstance(algorithm);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private String getAlgorithm(String configAlgorithm) {
        switch (configAlgorithm) {
            case "AES-192-GCM":
            case "AES-256-GCM":
                return "AES/GCM/NoPadding";
            default:
                return "AES/GCM/NoPadding"; // 默认使用GCM模式
        }
    }

    private int getKeyLength(String configAlgorithm) {
        switch (configAlgorithm) {
            case "AES-192-GCM":
                return 24;
            case "AES-256-GCM":
                return 32;
            default:
                return 32; // 默认使用256位密钥
        }
    }
}
