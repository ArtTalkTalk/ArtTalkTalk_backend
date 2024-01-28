package org.example.youth_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class YouthBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouthBeApplication.class, args);
    }

}
