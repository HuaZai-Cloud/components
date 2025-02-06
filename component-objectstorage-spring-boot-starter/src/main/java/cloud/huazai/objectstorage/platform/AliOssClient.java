package cloud.huazai.objectstorage.platform;

import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;
import com.aliyun.core.utils.IOUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * AliyunOssClient
 *
 * @author devon
 * @since 2025/2/6
 */

public class AliOssClient implements ObjectStorageClient {
    private final OSS ossClient;
    private final String bucket;

    public AliOssClient(ObjectStoragePlatformProperties properties) {
        this.ossClient = new OSSClientBuilder().build(properties.getEndpoint(), properties.getAccessKey(), properties.getSecretKey());
        this.bucket = properties.getBucket();
    }


    @Override
    public String uploadFile(String fileName, String filePath, InputStream inputStream) {

        ossClient.putObject(bucket, filePath+fileName, inputStream);

        // 设置 URL 过期时间为 1 小时。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以 GET 方法访问的签名 URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucket, filePath+fileName, expiration);

        return url.toString();
    }
}
