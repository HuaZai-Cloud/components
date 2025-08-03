package cloud.huazai.dataaccesslayer.mybatis.config;

import cloud.huazai.dataaccesslayer.mybatis.core.interceptor.JsonFieldInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisJsonFieldAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/1 17:47
 */
@Configuration
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnClass({SqlSessionFactory.class, JsonFieldInterceptor.class})
public class MyBatisJsonFieldAutoConfiguration  {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisJsonFieldAutoConfiguration.class);

    private final SqlSessionFactory sqlSessionFactory;

    private final JsonFieldInterceptor jsonFieldInterceptor; // 直接注入插件



    @PostConstruct
    public void addJsonFieldInterceptor() {
        try {
            // 获取 MyBatis 配置
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();

            // 检查插件是否已添加
            if (!configuration.getInterceptors().contains(jsonFieldInterceptor)) {
                configuration.addInterceptor(jsonFieldInterceptor);
                logger.info("JsonFieldInterceptor has been registered to MyBatis Configuration.");
            } else {
                logger.info("JsonFieldInterceptor is already registered.");
            }
        } catch (Exception e) {
            logger.error("Failed to register JsonFieldInterceptor", e);
        }
    }
}
