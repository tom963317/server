package com.gzlh;

import com.gzlh.netty.NettyConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.io.File;
import java.util.Properties;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SlaveServerApplication implements CommandLineRunner {
    @Resource
    private NettyConfig nettyConfig;
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        String rootPath = properties.getProperty("user.dir");
        SpringApplication application = new SpringApplication(SlaveServerApplication.class);
        application.addListeners(new ApplicationPidFileWriter(rootPath + File.separator + "app.pid"));
        //initApk();
        application.run(args);
    }


    @Override
    public void run(String... args) throws Exception {
        nettyConfig.connect();
    }
}
