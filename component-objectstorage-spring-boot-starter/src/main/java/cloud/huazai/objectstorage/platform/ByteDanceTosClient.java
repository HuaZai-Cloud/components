package cloud.huazai.objectstorage.platform;

import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;

import java.io.InputStream;
import java.util.Date;

public class ByteDanceTosClient implements ObjectStorageClient {

    private static volatile TOSV2 tosClient;
    private static String bucket;

    public ByteDanceTosClient(ObjectStoragePlatformProperties properties) {
        if (tosClient == null) {
            synchronized (AliOssClient.class) {
                if (tosClient == null) {
                    tosClient = new TOSV2ClientBuilder().build(properties.getRegion(), properties.getEndpoint(), properties.getAccessKey(), properties.getSecretKey());
                    bucket = properties.getBucket();
                }
            }
        }
    }

    @Override
    public String uploadFile(String filePath, String fileName, InputStream inputStream, Date expiration) {
        return "";
    }

    @Override
    public String multipartUploadFile(String filePath, String fileName, long fileSize, long partSize, InputStream inputStream, Date expiration) {
        return "";
    }

    @Override
    public String getSignedUrl(String filePath, String fileName, Date expiration) {
        return "";
    }

    @Override
    public void shutdownClient() {

    }
}
