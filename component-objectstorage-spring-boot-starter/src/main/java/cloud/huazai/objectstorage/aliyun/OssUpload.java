package cloud.huazai.objectstorage.aliyun;

import cloud.huazai.objectstorage.config.ObjectStorageProperties;
import cloud.huazai.objectstorage.config.Platform;
import cloud.huazai.objectstorage.object.ObjectStorageUpload;
import com.aliyun.oss.OSS;

import com.aliyun.oss.model.PutObjectRequest;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * OssUpload
 *
 * @author devon
 * @since 2025/1/22
 */

@Component
public class OssUpload implements ObjectStorageUpload {

    @Resource
    private OSS ossClient;

    @Resource
    private ObjectStorageProperties properties;

    @Override
    public String basicsUpload(String bucketName, File file) {

        String fileName = file.getName();

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return basicsUpload(bucketName, inputStream, fileName);

    }

    @Override
    public String basicsUpload(String bucketName, MultipartFile file) {
        String fileName = file.getName();

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return  basicsUpload(bucketName, inputStream, fileName);
    }

    @Override
    public String basicsUpload(String bucketName, InputStream inputStream, String name) {
        ObjectStorageProperties.StorageConfig aliyunConfig = properties.getPlatformMap().get(Platform.ALIYUN.getPlatform());

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, name, inputStream);

        // 创建PutObject请求。
        try {
            ossClient.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String objectKeyFromRequest = putObjectRequest.getKey();

        return "https://" + bucketName + "." + aliyunConfig.getEndpoint() + "/" + objectKeyFromRequest;
    }

    @PreDestroy
    public void destroy() {
        // 在 Spring 容器关闭时关闭 OSS 客户端
        if (ossClient!= null) {
            ossClient.shutdown();
        }
    }
}
