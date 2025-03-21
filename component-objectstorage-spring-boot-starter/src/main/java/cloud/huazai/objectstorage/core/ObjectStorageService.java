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

    public String uploadFile(ObjectStoragePlatform platform, String filePath, MultipartFile multipartFile) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return uploadFile(platform, filePath, multipartFile, expiration);
    }

    public String uploadFile(ObjectStoragePlatform platform, String filePath, MultipartFile multipartFile, Date expiration) {
        try {
            return uploadFile(platform,  filePath,multipartFile.getOriginalFilename(), multipartFile.getInputStream(), expiration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadFile(ObjectStoragePlatform platform, String filePath, File file) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return uploadFile(platform, filePath, file, expiration);
    }

    public String uploadFile(ObjectStoragePlatform platform,  String filePath,File file, Date expiration) {
        try {
            return uploadFile(platform,  filePath,file.getName(), new FileInputStream(file), expiration);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadFile(ObjectStoragePlatform platform,  String filePath,String fileName, InputStream inputStream) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return uploadFile(platform, filePath,fileName, inputStream,expiration);
    }

    public String uploadFile(ObjectStoragePlatform platform,String filePath, String fileName,  InputStream inputStream , Date expiration) {
        ObjectStorageClient client = factory.getClient(platform);
        return client.uploadFile(fileName, filePath, inputStream,expiration);
    }

    public  String multipartUploadFile(ObjectStoragePlatform platform,String filePath, File file){
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return multipartUploadFile(platform, filePath, file, expiration);
    }

    public  String multipartUploadFile(ObjectStoragePlatform platform,String filePath, File file, Date expiration){
         long partSize = 100 * 1024 * 1024L;
        return multipartUploadFile(platform,filePath,partSize,file,expiration);
    }

    public  String multipartUploadFile(ObjectStoragePlatform platform,String filePath,long partSize, File file, Date expiration){
        try {
            return multipartUploadFile(platform, filePath, file.getName(), file.length(), partSize, new FileInputStream(file), expiration);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public  String multipartUploadFile(ObjectStoragePlatform platform,String filePath, MultipartFile file){
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        return multipartUploadFile(platform, filePath, file, expiration);
    }

    public  String multipartUploadFile(ObjectStoragePlatform platform,String filePath, MultipartFile file, Date expiration){
        long partSize = 100 * 1024 * 1024L;
        return multipartUploadFile(platform, filePath, partSize,file, expiration);
    }

    public  String multipartUploadFile(ObjectStoragePlatform platform,String filePath,long partSize, MultipartFile file, Date expiration){
        try {
            return multipartUploadFile(platform, filePath, file.getName(), file.getSize(), partSize, file.getInputStream(), expiration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  String multipartUploadFile(ObjectStoragePlatform platform,String filePath,String fileName,long fileSize,long partSize , InputStream inputStream, Date expiration){
        ObjectStorageClient client = factory.getClient(platform);
        return client.multipartUploadFile( filePath,fileName, fileSize,partSize,inputStream,expiration);
    }



    public String getSignedUrl(ObjectStoragePlatform platform,String filePath, String fileName,  Date expiration) {
        ObjectStorageClient client = factory.getClient(platform);
        return client.getSignedUrl(filePath, fileName,  expiration);
    }

    public void shutdownObjectStorageClient(ObjectStoragePlatform platform) {
        ObjectStorageClient client = factory.getClient(platform);
        client.shutdownClient();
    }
}
