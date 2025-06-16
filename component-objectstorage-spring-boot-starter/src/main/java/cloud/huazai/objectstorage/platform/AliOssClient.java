package cloud.huazai.objectstorage.platform;

import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;
import cloud.huazai.objectstorage.util.ObjectStorageUtils;
import cloud.huazai.tool.java.lang.StringUtils;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AliyunOssClient
 *
 * @author devon
 * @since 2025/2/6
 */

public class AliOssClient implements ObjectStorageClient {
    private static volatile OSS ossClient;
    private static String bucket;

    private AliOssClient() {}

    AliOssClient(ObjectStoragePlatformProperties properties) {
        if (ossClient == null) {
            synchronized (AliOssClient.class) {
                if (ossClient == null) {
                    ossClient = OSSClientBuilder.create()
                            .clientConfiguration(new ClientBuilderConfiguration())
                            .credentialsProvider(new DefaultCredentialProvider(properties.getAccessKey(), properties.getSecretKey()))
                            .endpoint(properties.getEndpoint())
                            .region(properties.getRegion())
                            .build();
                    bucket = properties.getBucket();
                }
            }
        }
    }




    @Override
    public String uploadFile(String filePath, String fileName, InputStream inputStream, Date expiration) {

        String contentType = ObjectStorageUtils.getContentTypeByFileName(fileName);
        ObjectMetadata metadata = null;
        if (StringUtils.isNotBlank(contentType)) {
            metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
        }

        ossClient.putObject(bucket, filePath + fileName, inputStream, metadata);
        return getSignedUrl(filePath,fileName,  expiration);
    }

    @Override
    public String multipartUploadFile(String filePath,String fileName,  long fileSize, long partSize, InputStream inputStream, Date expiration) {
       String filePathAndName = filePath + fileName;
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, filePathAndName);
        String contentType = ObjectStorageUtils.getContentTypeByFileName(fileName);
        ObjectMetadata metadata = null;
        if (StringUtils.isNotBlank(contentType)) {
            metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
        }
        request.setObjectMetadata(metadata);

        try {
            InitiateMultipartUploadResult multipartUploadResult = ossClient.initiateMultipartUpload(request);
            String uploadId = multipartUploadResult.getUploadId();
            List<PartETag> partETags = new ArrayList<>();
            int partCount = (int) (fileSize / partSize);
            if (fileSize % partSize != 0) {
                partCount++;
            }
            // 遍历分片上传。
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileSize - startPos) : partSize;
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucket);
                uploadPartRequest.setKey(filePathAndName);
                uploadPartRequest.setUploadId(uploadId);
                inputStream.skip(startPos);
                uploadPartRequest.setInputStream(inputStream);
                uploadPartRequest.setPartSize(curPartSize);
                uploadPartRequest.setPartNumber(i + 1);
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                partETags.add(uploadPartResult.getPartETag());
            }
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucket, filePathAndName, uploadId, partETags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getSignedUrl(fileName, filePath, expiration);
    }

    @Override
    public void deleteFile(String filePath, String fileName) {
        ossClient.deleteObject(bucket, filePath + fileName);
    }

    @Override
    public String getSignedUrl(String filePath,String fileName,  Date expiration) {

        URL url = ossClient.generatePresignedUrl(bucket, filePath + fileName, expiration);
        return url.toString();
    }






    @Override
    public void shutdownClient() {
        if (ossClient != null) {
            ossClient.shutdown();
            ossClient = null;
        }
    }


}
