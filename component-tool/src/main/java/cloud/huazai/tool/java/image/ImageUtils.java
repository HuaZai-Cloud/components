package cloud.huazai.tool.java.image;

import cloud.huazai.tool.java.util.IOUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * ImageUtils
 *
 * @author Wu Di
 * @since 2025-05-23 13:38
 */
public class ImageUtils {


    /**
     * compressImage
     * @param inputStream inputStream
     * @param outputStream outputStream
     * @param quality quality
     */
    public static void compressImage(InputStream inputStream, OutputStream outputStream, float quality) {

        ImageWriter writer = null;
        ImageOutputStream ios = null;
        try {
            BufferedImage image = ImageIO.read(inputStream);

            if (image.getColorModel().getPixelSize() != 24) {
                BufferedImage rgbImage = new BufferedImage(
                        image.getWidth(),
                        image.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );
                rgbImage.getGraphics().drawImage(image, 0, 0, null);
                image = rgbImage;
            }

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

            if (!writers.hasNext()) {
                throw new IllegalStateException("No writers found");
            }
            writer = writers.next();
            ios = ImageIO.createImageOutputStream(outputStream);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            writer.write(null, new IIOImage(image, null, null), param);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭相关的流
            IOUtils.safeClose(ios);
            IOUtils.safeDispose(writer);
        }
    }

}
