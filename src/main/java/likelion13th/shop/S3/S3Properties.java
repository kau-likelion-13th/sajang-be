package likelion13th.shop.S3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Properties {
    private String accessKey;
    private String secretKey;
    private String region;

    @NestedConfigurationProperty
    //.yml상에서 하위 경로에 있으므로 하위 객체로 매핑
    private S3 s3 = new S3();

    @Getter
    @Setter
    public static class S3 {
        private String bucket;  // cloud.aws.s3.bucket
    }
}