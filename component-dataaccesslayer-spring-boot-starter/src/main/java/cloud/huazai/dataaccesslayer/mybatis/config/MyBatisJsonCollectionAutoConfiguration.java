package cloud.huazai.dataaccesslayer.mybatis.config;

import cloud.huazai.dataaccesslayer.mybatis.core.interceptor.JsonCollectionPlugin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisJsonCollectionAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/1 17:47
 */
@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MyBatisJsonCollectionAutoConfiguration {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired(required = false)
    private ObjectMapper objectMapper; // 如果用户没有提供，可以创建一个默认的

    @PostConstruct
    public void addJsonCollectionPlugin() {
        // 如果没有用户提供的 ObjectMapper，则创建一个
        ObjectMapper mapper = this.objectMapper != null ? this.objectMapper : new ObjectMapper();

        // 创建插件实例
        JsonCollectionPlugin plugin = new JsonCollectionPlugin(mapper);

        // 获取当前 SqlSessionFactory 的 Configuration
        org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();

        // 检查插件是否已添加（避免重复）
        if (!configuration.getInterceptors().contains(plugin)) {
            configuration.addInterceptor(plugin);
            System.out.println("JsonCollectionPlugin 已自动注册到 MyBatis Configuration.");
        }
    }
}
