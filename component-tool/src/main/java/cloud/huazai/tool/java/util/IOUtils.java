package cloud.huazai.tool.java.util;

import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils
 *
 * @author devon
 * @since 2025-05-23 15:27
 */
public class IOUtils {

    public static void safeClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }

    }
    // ImageInputStream
    public static void safeClose(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ignored) {
            }
        }

    }

    public static void safeClose(ImageInputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }

    }

    public static void safeDispose(ImageWriter writer) {
        if (writer != null) {
            writer.dispose();
        }
    }
}
