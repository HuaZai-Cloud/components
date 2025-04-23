package cloud.huazai.objectstorage.util;

public class ObjectStorageUtils {

    public static String getContentTypeByFileName(String fileName) {

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
