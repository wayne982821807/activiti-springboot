package com.lizhen.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * @author         :wangyz
 * @date     : 2018/11/22 16:10
 * 数据库连接池配置
 */
@Configuration
@Conditional(ProfilesActiveCondition.class)
public class DataSourceConfig {

    @PostConstruct
    public void init(){
        System.out.println("success");
    }

    @Component
    @Conditional(ProfilesActiveCondition.class)
    class SecretProperty {
        @Value("${datasource.profile.path}")
        private String dataSourceProfilePath;
        private Map accountFile;

        private Map getAccountFile() {
            if (accountFile != null) {
                return accountFile;
            }
            //加载账户配置文件
            File file;
            try {
                file = ResourceUtils.getFile(dataSourceProfilePath);
                Yaml yaml = new Yaml();
                accountFile = yaml.loadAs(new FileInputStream(file), Map.class);
                return accountFile;
            } catch (Exception e) {
                throw new RuntimeException("加载数据库配置文件失败！", e);
            }
        }
        /**
         * @param attr 属性值
         * 获取字符串
         * @return String
         */
        public String getStringValue(String attr) {
            return MapUtils.getString(getAccountFile(), attr);
        }

        public int getIntValue(String attr) {
            return MapUtils.getIntValue(getAccountFile(), attr);
        }

        /**
         * @param prefix 前缀
         * @param attr 属性名
         * @return String
         * 根据前缀获取属性值
         */
        private String getStringValue(String prefix, String attr) {
            return MapUtils.getString((Map) getAccountFile().get(prefix), attr);
        }

        private int getIntValue(String prefix, String attr) {
            return MapUtils.getIntValue((Map) getAccountFile().get(prefix), attr);
        }
    }
    @Autowired(required = false)
    private SecretProperty secretProperty;

    @Bean
    @Primary
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(secretProperty.getStringValue("druid", "url"));
        dataSource.setDriverClassName(secretProperty.getStringValue("druid", "driver-class-name"));
        dataSource.setUsername(secretProperty.getStringValue("druid", "username"));
        dataSource.setPassword(secretProperty.getStringValue("druid", "password"));
        dataSource.setInitialSize(secretProperty.getIntValue("druid", "initial-size"));
        dataSource.setMaxActive(secretProperty.getIntValue("druid", "max-active"));
        dataSource.setMinIdle(secretProperty.getIntValue("druid", "min-idle"));
        dataSource.setMaxWait(secretProperty.getIntValue("druid", "max-wait"));
        return dataSource;
    }

}
