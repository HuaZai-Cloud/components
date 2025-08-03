package cloud.huazai.dataaccesslayer.mybatis.core.encrypt;

/**
 * CustomCryptoHandler
 *
 * @author Devon
 * @since 2025/8/1 13:14
 */

public interface EncryptHandler {

    String encrypt(String plaintext) throws Exception;
    String decrypt(String ciphertext) throws Exception;
}
