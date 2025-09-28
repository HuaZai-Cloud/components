package cloud.huazai.tool.java.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * FileUtils
 *
 * @author Devon
 * @since 2025/9/28 15:07
 */

public class FileUtils {

    public static void writeToFile(String path, String content) throws IOException {
        Files.write(Paths.get(path), content.getBytes(), StandardOpenOption.CREATE);
    }

    public static String readFromFile(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }

    public static List<String> readLines(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    public static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    public static void delete(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

    public static void copy(String src, String dest) throws IOException {
        Files.copy(Paths.get(src), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
    }
}
