package br.com.pnipapi.config;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Data
@ConfigurationProperties("api")
@Validated
public class ApiProperties {

    @URL
    @NotBlank(message = "URL do servico de planilhas não pode ser null")
    private String planilhasBaseUrl;

    @URL
    @NotBlank(message = "URL do servico de planilhas não pode ser null")
    private String notificationBaseUrl;

    @NotBlank(message = "app-home não pode ser vazio ou null")
    private String appHome;
    private boolean checkHomeDirs = true;

    @Valid
    private Aws aws;

    private int maxTimeout = 600000;
    private int maxPoolConnection = 200;
    private int initialPoolConnection = 15;

    @Data
    public static class Aws implements AwsCredentials {
        @NotBlank
        @Pattern(regexp = "[\\w]+")
        @Length(min = 16, max = 128)
        private String accessKeyId;
        @NotBlank
        private String secretAccessKey;

        private String bucketName;

        @Override
        public String accessKeyId() {
            return this.accessKeyId;
        }

        @Override
        public String secretAccessKey() {
            return this.secretAccessKey;
        }

        public AwsCredentialsProvider toStaticCredentialsProvider() {
            return StaticCredentialsProvider.create(this);
        }
    }
    private Sign sign;
    @Data
    public static class Sign {

        private String token;

        private String assinatura;

        private String clientId;

        private String secret;

        private String verificador;

    }

}
