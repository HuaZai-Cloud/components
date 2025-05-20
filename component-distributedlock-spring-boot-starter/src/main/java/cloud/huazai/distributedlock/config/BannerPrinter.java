package cloud.huazai.distributedlock.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

/**
 * BannerPrinter
 *
 * @author Wu Di
 * @since 2025-05-20 14:19
 */
@Component
public class BannerPrinter implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        printBanner();
    }

    private void printBanner() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("META-INF/maven/cloud.huazai.component/component-distributedlock-spring-boot-starter/pom.properties");
            if (inputStream != null) {
                Properties properties = new Properties();
                properties.load(inputStream);
                String version = properties.getProperty("version", "1.0.0");

                try (InputStream bannerStream = getClass().getClassLoader().getResourceAsStream("distributedlock-banner.txt")) {
                    if (bannerStream != null) {
                        String content = new String(bannerStream.readAllBytes());
                        // 替换占位符为实际版本号
                        content = content.replace("${version}", version);
                        System.out.println(content);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
