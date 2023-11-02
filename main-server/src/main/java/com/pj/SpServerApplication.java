package com.pj;


import com.pj.current.config.SystemObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * 启动
 *
 * @author kong
 */
@EnableCaching // 启用缓存
//@EnableScheduling // 启动定时任务
@EnableTransactionManagement // 启动注解事务管理
@EnableRetry
@EnableAsync
@SpringBootApplication(scanBasePackages = {"com"})
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@Slf4j
public class SpServerApplication {

    public static void main(String[] args) throws IOException {
        Properties properties = System.getProperties();
        String rootPath = properties.getProperty("user.dir");
        SpringApplication application = new SpringApplication(SpServerApplication.class);
        application.addListeners(new ApplicationPidFileWriter(rootPath + File.separator + "app.pid"));
        application.run(args);
    }


}
