package cloud.huazai.tool.java.image;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

public class ImageUtilsTest {

    @Test
    void compressImage() throws Exception {

        // ByteArrayOutputStream outputStream = ImageUtils.compressImage(Files.newInputStream(new File("/Users/devon/Documents/壁纸.png").toPath()), 0.5f);
        //
        // File file = new File("/Users/devon/Documents/壁纸5.png");
        // OutputStream fileOutputStream = new FileOutputStream(file);
        //
        // outputStream.writeTo(fileOutputStream);
        File file = new File("/Users/devon/Documents/壁纸.png");
        String fileName = file.getName();
        // 获取文件后缀
        String suffix = "";
        if (fileName.contains(".")) {
            suffix = fileName.substring(fileName.lastIndexOf("."));
        } else {
            suffix = ".png";
        }
        InputStream inputStream = Files.newInputStream(new File("/Users/devon/Documents/壁纸.png").toPath());
        FileOutputStream fileOutputStream = new FileOutputStream(File.createTempFile(String.valueOf(System.currentTimeMillis()), suffix));
        ImageUtils.compressImage(inputStream, fileOutputStream, 0.5f);


    }
}