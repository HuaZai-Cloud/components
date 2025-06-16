package cloud.huazai.tool.java.image;

import cloud.huazai.tool.java.util.IOUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     *
     * @param inputStream  inputStream
     * @param outputStream outputStream
     * @param quality      quality
     */
    public static void compressImage(InputStream inputStream, OutputStream outputStream, float quality) {

        ImageOutputStream imageOutputStream = null;
        try {
            BufferedImage image = ImageIO.read(inputStream);
            imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            compressImage(image,imageOutputStream, quality);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭相关的流
            IOUtils.safeClose(imageOutputStream);
        }
    }


    public static void compressImage(File inputFile, File outputFile, float quality) {

        // 检查输入文件是否存在
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("Input file does not exist.");
        }

        ImageOutputStream imageOutputStream = null;
        try {
            // 获取输入图像
            BufferedImage image = ImageIO.read(inputFile);
            imageOutputStream = ImageIO.createImageOutputStream(outputFile);
            compressImage(image,imageOutputStream,quality);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.safeClose(imageOutputStream);
        }
    }



    private static void compressImage(BufferedImage image, ImageOutputStream imageOutputStream, float quality)  {
        if (image.getColorModel().getPixelSize() != 24) {
            BufferedImage rgbImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            rgbImage.getGraphics().drawImage(image, 0, 0, null);
            image = rgbImage;
        }
        // 获取所有可用的图像写入器
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found");
        }

        ImageWriter writer = writers.next();

        writer.setOutput(imageOutputStream);

        // 设置压缩参数
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality); // 压缩质量

        // 写入图像
        try {
            writer.write(null, new IIOImage(image, null, null), param);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.safeDispose(writer);
        }
    }


    public static BufferedImage rotateImage(BufferedImage image, double angle) {
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
        int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.translate((newWidth - image.getWidth()) / 2, (newHeight - image.getHeight()) / 2);
        g2d.rotate(radians, (double) image.getWidth() / 2, (double) image.getHeight() / 2);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

}
