package cloud.huazai.objectstorage.ali;

import cloud.huazai.objectstorage.object.ObjectStorageClient;
import cloud.huazai.objectstorage.object.ObjectStorageConfig;
import com.aliyun.oss.OSS;
import io.github.artislong.core.ali.model.AliOssConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AliOssClient
 *
 * @author devon
 * @since 2025/1/24
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AliOssClient implements ObjectStorageClient {

    private OSS oss;
    private ObjectStorageConfig aliOssProperties;
}
