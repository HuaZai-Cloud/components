package cloud.huazai.dataaccesslayer.mybatis.core.interceptor;

import cloud.huazai.dataaccesslayer.mybatis.annotation.Encrypt;
import cloud.huazai.dataaccesslayer.mybatis.core.crypto.CryptoHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EncryptFieldInterceptor
 *
 * @author Devon
 * @since 2025/8/1 12:00
 */
public class EncryptFieldInterceptor implements InnerInterceptor {

    private final CryptoHandler cryptoHandler;

    public EncryptFieldInterceptor(CryptoHandler cryptoHandler) {
        if (cryptoHandler == null) {
            throw new IllegalArgumentException("CryptoHandler must not be null");
        }
        this.cryptoHandler = cryptoHandler;
    }



    private void encryptObject(Object obj) {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();

        for (Field field : getAllFields(clazz)) {
            if (field.getType() == String.class && field.isAnnotationPresent(Encrypt.class)) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(obj);
                    if (value != null && !isEncrypted(value)) {
                        String encrypted = cryptoHandler.encrypt(value);
                        field.set(obj, "ENC(" + encrypted + ")");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("加密失败: " + field.getName(), e);
                }
            }
        }
    }

    private void decryptObject(Object obj) {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();

        for (Field field : getAllFields(clazz)) {
            if (field.getType() == String.class && field.isAnnotationPresent(Encrypt.class)) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(obj);
                    if (value != null && isEncrypted(value)) {
                        String ciphertext = value.substring(4, value.length() - 1); // 去掉 ENC( 和 )
                        String decrypted = cryptoHandler.decrypt(ciphertext);
                        field.set(obj, decrypted);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("解密失败: " + field.getName(), e);
                }
            }
        }
    }

    // 辅助方法获取所有字段（包括父类）
    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    // 判断是否已经是加密后的数据
    private boolean isEncrypted(String value) {
        return value.startsWith("ENC(") && value.endsWith(")");
    }


}
