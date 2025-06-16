package cloud.huazai.objectstorage.core;

import cloud.huazai.objectstorage.constant.ObjectStoragePlatform;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

/**
 * ObjectStorageService
 *
 * @author devon
 * @since 2025/2/6
 */

@Service
public class ObjectStorageService {
    private final ObjectStorageFactory factory;

    public ObjectStorageService(ObjectStorageFactory factory) {
        this.factory = factory;
    }

    /**
     * 普通上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param multipartFile 文件
     * @return 签名url
     */
    public String uploadFile(ObjectStoragePlatform platform, String filePath, MultipartFile multipartFile) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return uploadFile(platform, filePath, multipartFile, expiration);
    }

    /**
     * 普通上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param multipartFile 文件
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String uploadFile(ObjectStoragePlatform platform, String filePath, MultipartFile multipartFile, Date expiration) {
        try {
            return uploadFile(platform, filePath, multipartFile.getOriginalFilename(), multipartFile.getInputStream(), expiration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 普通上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param file 文件
     * @return 签名url
     */
    public String uploadFile(ObjectStoragePlatform platform, String filePath, File file) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return uploadFile(platform, filePath, file, expiration);
    }

    /**
     * 普通上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param file 文件
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String uploadFile(ObjectStoragePlatform platform, String filePath, File file, Date expiration) {
        try {
            return uploadFile(platform, filePath, file.getName(), new FileInputStream(file), expiration);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 普通上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param fileName 文件名称
     * @param inputStream 输入流
     * @return 签名url
     */
    public String uploadFile(ObjectStoragePlatform platform, String filePath, String fileName, InputStream inputStream) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return uploadFile(platform, filePath, fileName, inputStream, expiration);
    }

    /**
     * 普通上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param fileName 文件名称
     * @param inputStream 输入流
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String uploadFile(ObjectStoragePlatform platform, String filePath, String fileName, InputStream inputStream, Date expiration) {
        ObjectStorageClient client = factory.getClient(platform);
        return client.uploadFile(fileName, filePath, inputStream, expiration);
    }

    /**
     * 分片上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param file 文件
     * @return 签名url
     */
    public String multipartUploadFile(ObjectStoragePlatform platform, String filePath, File file) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return multipartUploadFile(platform, filePath, file, expiration);
    }

    /**
     * 分片上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param file 文件
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String multipartUploadFile(ObjectStoragePlatform platform, String filePath, File file, Date expiration) {
        long partSize = 100 * 1024 * 1024L;
        return multipartUploadFile(platform, filePath, partSize, file, expiration);
    }

    /**
     * 分片上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param partSize 每片大小
     * @param file 文件
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String multipartUploadFile(ObjectStoragePlatform platform, String filePath, long partSize, File file, Date expiration) {
        try {
            return multipartUploadFile(platform, filePath, file.getName(), file.length(), partSize, new FileInputStream(file), expiration);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分片上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param file 文件
     * @return 签名url
     */
    public String multipartUploadFile(ObjectStoragePlatform platform, String filePath, MultipartFile file) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return multipartUploadFile(platform, filePath, file, expiration);
    }

    /**
     * 分片上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param file 文件
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String multipartUploadFile(ObjectStoragePlatform platform, String filePath, MultipartFile file, Date expiration) {
        long partSize = 100 * 1024 * 1024L;
        return multipartUploadFile(platform, filePath, partSize, file, expiration);
    }

    /**
     * 分片上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param partSize 每片大小
     * @param file 文件
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String multipartUploadFile(ObjectStoragePlatform platform, String filePath, long partSize, MultipartFile file, Date expiration) {
        try {
            return multipartUploadFile(platform, filePath, file.getName(), file.getSize(), partSize, file.getInputStream(), expiration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分片上传
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @param partSize 每片大小
     * @param inputStream 输入流
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String multipartUploadFile(ObjectStoragePlatform platform, String filePath, String fileName, long fileSize, long partSize, InputStream inputStream, Date expiration) {
        ObjectStorageClient client = factory.getClient(platform);
        return client.multipartUploadFile(filePath, fileName, fileSize, partSize, inputStream, expiration);
    }

    /**
     * 删除文件
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param fileName 文件名称
     */
    public void deleteFile(ObjectStoragePlatform platform, String filePath, String fileName) {
        ObjectStorageClient client = factory.getClient(platform);
        client.deleteFile(filePath, fileName);
    }

    /**
     * 获取签名url
     * @param platform 平台
     * @param filePath 对象存储路径
     * @param fileName 文件名称
     * @param expiration 签名url到期时间
     * @return 签名url
     */
    public String getSignedUrl(ObjectStoragePlatform platform, String filePath, String fileName, Date expiration) {
        ObjectStorageClient client = factory.getClient(platform);
        return client.getSignedUrl(filePath, fileName, expiration);
    }

    /**
     * 关闭对象存储客户端
     * @param platform 平台
     */
    public void shutdownObjectStorageClient(ObjectStoragePlatform platform) {
        ObjectStorageClient client = factory.getClient(platform);
        client.shutdownClient();
    }
}
