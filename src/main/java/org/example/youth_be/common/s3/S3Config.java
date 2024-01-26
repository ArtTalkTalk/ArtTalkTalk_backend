package org.example.youth_be.common.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(s3Properties.getCredentials().getAccessKey(),
                s3Properties.getCredentials().getSecretKey());

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withRegion(s3Properties.getRegion().getStatic_())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

}