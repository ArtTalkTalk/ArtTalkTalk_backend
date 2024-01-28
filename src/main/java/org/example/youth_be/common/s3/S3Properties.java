package org.example.youth_be.common.s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Properties {

        private final S3 s3;
        private final Credentials credentials;
        private final Region region;
        private final UploadDirs uploadDirs;

        public S3Properties(S3 s3, Credentials credentials, Region region, UploadDirs uploadDirs) {
            this.s3 = s3;
            this.credentials = credentials;
            this.region = region;
            this.uploadDirs = uploadDirs;
        }

        @Getter
        @Setter
        public static class S3 {
            private String bucket;
        }


        @Getter
        @Setter
        public static class Credentials {
            private String accessKey;
            private String secretKey;
        }

        @Getter
        @Setter
        public static class Region {
            private String static_;
        }

        @Getter
        @Setter
        public static class UploadDirs {
            private String profileDirName;
        }
}
