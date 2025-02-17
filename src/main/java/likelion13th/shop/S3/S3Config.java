package likelion13th.shop.S3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // Spring 설정 클래스임을 명시
@EnableConfigurationProperties(S3Properties.class)  // 명시적 등록
@RequiredArgsConstructor
public class S3Config {
    private final S3Properties s3Properties;

    @Bean
    public AmazonS3 amazonS3() {
        if (s3Properties.getRegion() == null) {
            throw new IllegalStateException("AWS Region이 설정되지 않았습니다.");
        }

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(
                s3Properties.getAccessKey(),
                s3Properties.getSecretKey()
        );
        return AmazonS3ClientBuilder.standard()
                .withRegion(s3Properties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

}