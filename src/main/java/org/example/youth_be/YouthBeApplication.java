package org.example.youth_be;

import org.example.youth_be.common.s3.S3Config;
import org.example.youth_be.common.s3.S3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({S3Properties.class})
public class YouthBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouthBeApplication.class, args);
    }

}
