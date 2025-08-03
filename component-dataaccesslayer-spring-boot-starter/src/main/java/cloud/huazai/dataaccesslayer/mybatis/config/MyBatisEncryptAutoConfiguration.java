package cloud.huazai.dataaccesslayer.mybatis.config;

import cloud.huazai.dataaccesslayer.mybatis.core.interceptor.EncryptFieldInterceptor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * MyBatisEncryptAutoConfiguration
 *
 * @author Devon
 * @since 2025/8/2 15:05
 */

@Configuration
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnClass(SqlSessionFactory.class)
@AutoConfigureAfter(com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class)
public class MyBatisEncryptAutoConfiguration  {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisEncryptAutoConfiguration.class);

    private final SqlSessionFactory sqlSessionFactory;

    private final EncryptFieldInterceptor encryptFieldInterceptor;


    @PostConstruct
    public void addEncryptInterceptor() {
        org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();

        // 检查插件是否已添加，避免重复注册
        if (!configuration.getInterceptors().contains(encryptFieldInterceptor)) {
            configuration.addInterceptor(encryptFieldInterceptor);
            logger.info("EncryptFieldInterceptor has been registered to MyBatis Configuration.");
        } else {
            logger.info("EncryptFieldInterceptor is already registered.");
        }
    }
}
