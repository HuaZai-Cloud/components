package cloud.huazai.tool.java.image;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
public class ImageUtilsTest {

    @Test
    void compressImage() throws Exception {

        ByteArrayOutputStream outputStream = ImageUtils.compressImage(Files.newInputStream(new File("/Users/devon/Documents/壁纸.png").toPath()), 0.5f);

        File file = new File("/Users/devon/Documents/壁纸5.png");
        OutputStream fileOutputStream = new FileOutputStream(file);

        outputStream.writeTo(fileOutputStream);

    }
}