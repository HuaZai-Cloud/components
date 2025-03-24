package cloud.huazai.objectstorage.platform;

import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;
import cloud.huazai.tool.java.lang.StringUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
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

    public AliOssClient(ObjectStoragePlatformProperties properties) {
        if (ossClient == null) {
            synchronized (AliOssClient.class) {
                if (ossClient == null) {
                    ossClient = new OSSClientBuilder().build(properties.getEndpoint(), properties.getAccessKey(), properties.getSecretKey());
                    bucket = properties.getBucket();
                }
            }
        }
    }


    @Override
    public String uploadFile(String filePath, String fileName, InputStream inputStream, Date expiration) {

        String contentType = getContentType(fileName);
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
        String contentType = getContentType(fileName);
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


    // --------------------- private ------------------------- private ----------------------------

    private String getContentType(String fileName) {

        // 图片
        if (fileName.toLowerCase().endsWith(".bmp")) {
            return "image/bmp";
        }
        if (fileName.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        }
        if (fileName.toLowerCase().endsWith(".jpeg") || fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".png")) {
            return "image/jpg";
        }

        if (fileName.toLowerCase().endsWith(".html")) {
            return "text/html";
        }
        if (fileName.toLowerCase().endsWith(".txt")) {
            return "text/plain";
        }

        if (fileName.toLowerCase().endsWith(".vsd")) {
            return "application/vnd.visio";
        }
        if (fileName.toLowerCase().endsWith(".pptx") || fileName.toLowerCase().endsWith(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (fileName.toLowerCase().endsWith(".docx") || fileName.toLowerCase().endsWith(".doc")) {
            return "application/msword";
        }
        if (fileName.toLowerCase().endsWith(".xml")) {
            return "text/xml";
        }
        if (fileName.toLowerCase().endsWith(".pdf")) {
            return "application/pdf";
        }
        // 视频
        if (fileName.toLowerCase().endsWith(".mp4")) {
            return "video/mp4";
        }
        if (fileName.toLowerCase().endsWith(".asf") || fileName.toLowerCase().endsWith(".asx")) {
            return "video/x-ms-asf";
        }
        if (fileName.toLowerCase().endsWith(".rm")) {
            return "application/vnd.rn-realmedia";
        }
        if (fileName.toLowerCase().endsWith(".rmvb")) {
            return "application/vnd.rn-realmedia-vbr";
        }
        if (fileName.toLowerCase().endsWith(".mov")) {
            return "video/quicktime";
        }
        if (fileName.toLowerCase().endsWith(".avi")) {
            return "video/avi";
        }
        if (fileName.toLowerCase().endsWith(".wmv")) {
            return "video/x-ms-wmv";
        }
        // 语音
        if (fileName.toLowerCase().endsWith(".mp3")) {
            return "audio/mp3";
        }
        return null;
    }
}
