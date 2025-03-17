package cloud.huazai.objectstorage.platform;

import cloud.huazai.objectstorage.core.ObjectStorageClient;
import cloud.huazai.objectstorage.properties.ObjectStoragePlatformProperties;
import com.aliyun.core.utils.IOUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        // 创建ObjectMetadata对象。
        ObjectMetadata metadata = new ObjectMetadata();
       String contentType  = getContentType(fileName);
        metadata.setContentType(contentType);

        ossClient.putObject(bucket, filePath+fileName, inputStream,metadata);

        // 设置 URL 过期时间为 1 小时。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以 GET 方法访问的签名 URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucket, filePath + fileName, expiration);

        return url.toString();
    }

    private String getContentType(String fileName) {

        if (fileName.toLowerCase().endsWith(".tif")) {
            return "image/tiff";
        }
         if (fileName.toLowerCase().endsWith(".a11")) {
            return "application/x-a11";
        }
         if (fileName.toLowerCase().endsWith(".acp")) {
            return "audio/x-mei-aac";
        }
         if (fileName.toLowerCase().endsWith(".ai")) {
            return "application/postscript";
        }
         if (fileName.toLowerCase().endsWith(".aif")) {
            return "audio/aiff";
        }

         if (fileName.toLowerCase().endsWith(".aifc")) {
            return "audio/aiff";
        }

         if (fileName.toLowerCase().endsWith(".aiff")) {
            return "audio/aiff";
        }
         if (fileName.toLowerCase().endsWith(".anv")) {
            return "application/x-anv";
        }
         if (fileName.toLowerCase().endsWith(".apk")) {
            return "application/vnd.android.package-archive";
        }

         if (fileName.toLowerCase().endsWith(".asa")) {
            return "text/asa";
        }
         if (fileName.toLowerCase().endsWith(".asf")) {
            return "video/x-ms-asf";
        }
         if (fileName.toLowerCase().endsWith(".asp")) {
            return "text/asp";
        }

         if (fileName.toLowerCase().endsWith(".asx")) {
             return "video/x-ms-asf";
         }
         if (fileName.toLowerCase().endsWith(".au")) {
             return "audio/basic";
         }
         if (fileName.toLowerCase().endsWith(".avi")) {
             return "video/avi";
         }
         if (fileName.toLowerCase().endsWith(".awf")) {
             return "application/vnd.adobe.workflow";
         }
         if (fileName.toLowerCase().endsWith(".biz")) {
             return "text/xml";
         }
         if (fileName.toLowerCase().endsWith(".bmp")) {
             return "application/x-bmp";
         }
         if (fileName.toLowerCase().endsWith(".bot")) {
             return "application/x-bot";
         }
         if (fileName.toLowerCase().endsWith(".c4t")) {
             return "application/x-c4t";
         }
         if (fileName.toLowerCase().endsWith(".c90")) {
             return "application/x-c90";
         }
         if (fileName.toLowerCase().endsWith(".cal")) {
             return "application/x-cals";
         }
         if (fileName.toLowerCase().endsWith(".cat")) {
             return "application/vnd.ms-pki.seccat";
         }
         if (fileName.toLowerCase().endsWith(".cdf")) {
             return "application/x-netcdf";
         }
         if (fileName.toLowerCase().endsWith(".cdr")) {
             return "application/x-cdr";
         }
         if (fileName.toLowerCase().endsWith(".cgi")) {
             return "application/x-cgi";
         }
         if (fileName.toLowerCase().endsWith(".cel")) {
            return "application/x-cel";
         }
         if (fileName.toLowerCase().endsWith(".cer")) {
             return "application/x-x509-ca-cert";
         }
         if (fileName.toLowerCase().endsWith(".cg4")) {
             return "application/x-g4";
         }
         if (fileName.toLowerCase().endsWith(".cgm")) {
             return "application/x-cgm";
         }
         if (fileName.toLowerCase().endsWith(".cit")) {
             return "application/x-cit";
         }
         if (fileName.toLowerCase().endsWith(".class")) {
             return "java/";
         }
         if (fileName.toLowerCase().endsWith(".cml")) {
             return "text/xml";
         }
         if (fileName.toLowerCase().endsWith(".cmp")) {
             return "application/x-cmp";
         }
         if (fileName.toLowerCase().endsWith(".cmx")) {
             return "application/x-cmx";
         }
        if (fileName.toLowerCase().endsWith(".cot")) {
            return "application/x-cot";
        }
        if (fileName.toLowerCase().endsWith(".crl")) {
            return "application/pkix-crl";
        }
        if (fileName.toLowerCase().endsWith(".crt")) {
            return "application/x-x509-ca-cert";
        }
        if (fileName.toLowerCase().endsWith(".csi")) {
            return "application/x-csi";
        }
        if (fileName.toLowerCase().endsWith(".css")) {
            return "text/css";
        }
        if (fileName.toLowerCase().endsWith(".csv")) {
            return "text/csv";
        }
        if (fileName.toLowerCase().endsWith(".cut")) {
            return "application/x-cut";
        }
        if (fileName.toLowerCase().endsWith(".dbf")) {
            return "application/x-dbf";
        }
        if (fileName.toLowerCase().endsWith(".dbm")) {
            return "application/x-dbm";
        }
        if (fileName.toLowerCase().endsWith(".dbx")) {
            return "application/x-dbx";
        }
        if (fileName.toLowerCase().endsWith(".dcd")) {
            return "text/xml";
        }
        if (fileName.toLowerCase().endsWith(".dcx")) {
            return "application/x-dcx";
        }
        if (fileName.toLowerCase().endsWith(".der")) {
            return "application/x-x509-ca-cert";
        }
        if (fileName.toLowerCase().endsWith(".dgn")) {
            return "application/x-dgn";
        }
        if (fileName.toLowerCase().endsWith(".dib")) {
            return "application/x-dib";
        }
        if (fileName.toLowerCase().endsWith(".dll")) {
            return "application/x-msdownload";
        }
        if (fileName.toLowerCase().endsWith(".doc")) {
            return "application/msword";
        }
        if (fileName.toLowerCase().endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        if (fileName.toLowerCase().endsWith(".dot")) {
            return "application/msword";
        }
        if (fileName.toLowerCase().endsWith(".dotx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
        }
        if (fileName.toLowerCase().endsWith(".drw")) {
            return "application/x-drw";
        }
        if (fileName.toLowerCase().endsWith(".dtd")) {
            return "text/xml";
        }

        if (fileName.toLowerCase().endsWith(".dwf")) {
            return "application/x-dwf";
        }
        if (fileName.toLowerCase().endsWith(".dwg")) {
            return "application/x-dwg";
        }
        if (fileName.toLowerCase().endsWith(".dxb")) {
            return "application/x-dxb";
        }
        if (fileName.toLowerCase().endsWith(".dxf")) {
            return "application/x-dxf";
        }
        if (fileName.toLowerCase().endsWith(".edn")) {
            return "application/vnd.adobe.edn";
        }
        if (fileName.toLowerCase().endsWith(".emf")) {
            return "application/x-emf";
        }
        if (fileName.toLowerCase().endsWith(".eml")) {
            return "message/rfc822";
        }
        if (fileName.toLowerCase().endsWith(".ent")) {
            return "text/xml";
        }
        if (fileName.toLowerCase().endsWith(".epi")) {
            return "application/x-epi";
        }
        if (fileName.toLowerCase().endsWith(".eps")) {
            return "application/postscript";
        }
        if (fileName.toLowerCase().endsWith(".etd")) {
            return "application/x-ebx";
        }



        return "application/octet-stream";
    }
}
