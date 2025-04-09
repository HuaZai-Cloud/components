package cloud.huazai.objectstorage.platform;

import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;
import cloud.huazai.objectstorage.util.ObjectStorageUtils;
import cloud.huazai.tool.java.date.DateUtils;
import cloud.huazai.tool.java.lang.StringUtils;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.model.object.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

public class ByteDanceTosClient implements ObjectStorageClient {

    private static volatile TOSV2 tosClient;
    private static String bucket;

    public ByteDanceTosClient(ObjectStoragePlatformProperties properties) {
        if (tosClient == null) {
            synchronized (AliOssClient.class) {
                if (tosClient == null) {
                    // 创建 TOSV2 客户端实例
                    tosClient = new TOSV2ClientBuilder()
                            .build(properties.getRegion(),
                                    properties.getEndpoint(),
                                    properties.getAccessKey(),
                                    properties.getSecretKey());
                    bucket = properties.getBucket();
                }
            }
        }
    }

    @Override
    public String uploadFile(String filePath, String fileName, InputStream inputStream, Date expiration) {

        PutObjectInput putObjectInput = new PutObjectInput().setBucket(bucket).setKey(filePath + fileName).setContent(inputStream);

        String contentType = ObjectStorageUtils.getContentTypeByFileName(fileName);
        if (StringUtils.isNotBlank(contentType)) {
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
            options.setContentType(contentType);
            options.setContentDisposition("inline");
            putObjectInput.setOptions(options);
        }


        tosClient.putObject(putObjectInput);
        return getSignedUrl(filePath, fileName, expiration);
    }

    @Override
    public String multipartUploadFile(String filePath, String fileName, long fileSize, long partSize, InputStream inputStream, Date expiration) {

        try{
            // 1. 初始化分片上传
            String uploadId = null;
            CreateMultipartUploadInput create = new CreateMultipartUploadInput().setBucket(bucket).setKey(filePath + fileName);
            String contentType = ObjectStorageUtils.getContentTypeByFileName(fileName);
            if (StringUtils.isNotBlank(contentType)) {
                ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
                options.setContentType(contentType);
                create.setOptions(options);
            }

            CreateMultipartUploadOutput createOutput = tosClient.createMultipartUpload(create);
            System.out.println("createMultipartUpload succeed, uploadId is " + createOutput.getUploadID());

            // 从 createMultipartUpload 结果中获取 uploadId，用于后续的分片上传和合并分片。
            uploadId = createOutput.getUploadID();



            // 已上传分片列表，每次上传分片后记录。
            List<UploadedPartV2> uploadedParts = new ArrayList<>();

            // 以下代码展示读取同一个文件到 FileInputStream，按照每 5MB 大小从头到尾读取文件的一部分进行上传。

            // fileSize 为文件总大小
            // offset 为读取文件的位置。如果 offset < fileSize，说明已读到文件末尾，不再读取上传（此时文件已上传完成）
            long offset = 0;
            for (int i = 1; offset < fileSize; ++i) {
                try(FileInputStream content = new FileInputStream(filePath);){
                    // 每次只上传文件的一部分，需要跳过前面已上传的部分，即为 offset 长度。
                    content.skip(offset);
                    InputStream wrappedContent = new TosRepeatableBoundedFileInputStream(content, partSize);
                    // 注意 partNumber 从 1 开始计数。
                    if (fileSize-offset < partSize) {
                        // 如果 skip 过后剩余的数据长度小于 partSize，即剩余数据长度没有达到 partSize
                        // 说明已到达最后一个分片，需要修改分片大小
                        partSize = fileSize-offset;
                    }
                    UploadPartV2Input input = new UploadPartV2Input().setBucket(bucket)
                            .setKey(filePath+fileName).setUploadID(uploadId).setPartNumber(i)
                            .setContentLength(partSize).setContent(wrappedContent);
                    UploadPartV2Output output = tosClient.uploadPart(input);
                    // 存储已上传分片信息，必须设置 partNumber 和 etag
                    uploadedParts.add(new UploadedPartV2().setPartNumber(i).setEtag(output.getEtag()));


                    // 每上传一次分片，需要更新 offset 的值
                    offset += partSize;
                } catch (IOException e) {
                    System.out.println("uploadPart read file failed");
                    e.printStackTrace();
                }
            }

            // 3. 合并已上传的分片
            // 需要设置桶名、对象名、uploadId和已上传的分片列表
            CompleteMultipartUploadV2Input complete = new CompleteMultipartUploadV2Input().setBucket(bucket)
                    .setKey(filePath+fileName).setUploadID(uploadId);
//            complete.setUploadedParts(uploadedParts);
            // 从 2.6.0 版本开始，SDK 支持设置 completeAll 参数
            // 如果设置 completeAll 为 true，则 TOS 会列举当前 uploadId 已上传的所有 part，
            // 并根据 partNumber 的序号排序执行 completeMultipartUpload 操作。
            // 如果设置 completeAll 为 true，则不允许调用 setUploadedParts 设置 uploadedParts，否则报错。
            // complete.setUploadedParts(null);
             complete.setCompleteAll(true);
            CompleteMultipartUploadV2Output completedOutput = tosClient.completeMultipartUpload(complete);
            System.out.printf("completeMultipartUpload succeed, etag is %s, crc64 value is %s, location is %s.\n",
                    completedOutput.getEtag(), completedOutput.getHashCrc64ecma(), completedOutput.getLocation());
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("uploadPart failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("uploadPart failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("uploadPart failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }

        return getSignedUrl(filePath, fileName, expiration);
    }

    @Override
    public void deleteFile(String filePath, String fileName) {
        DeleteObjectInput input = new DeleteObjectInput().setBucket(bucket).setKey(filePath + fileName);
        tosClient.deleteObject(input);
    }

    @Override
    public String getSignedUrl(String filePath, String fileName, Date expiration) {

        PreSignedURLInput input = new PreSignedURLInput();
        input.setHttpMethod(HttpMethod.GET);
        input.setBucket(bucket);
        input.setKey(filePath + fileName);
        input.setExpires(DateUtils.betweenSecond(LocalDateTime.now(),DateUtils.toLocalDateTime(expiration)));


        PreSignedURLOutput output = tosClient.preSignedURL(input);
        return output.getSignedUrl();
    }

    @Override
    public void shutdownClient() {

        if (tosClient != null) {
            try {
                tosClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                tosClient = null;
            }
        }
    }
}
