package br.com.pnipapi.service.storage;

import br.com.pnipapi.config.ApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;
import java.util.Optional;

@Configuration
public class StorageServiceConfiguration {
    private final ApiProperties apiProperties;
    private static final Logger        log = LoggerFactory.getLogger(StorageServiceConfiguration.class);

    public StorageServiceConfiguration(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    StorageService storageService() {
        final Optional<String> bucket = Optional.ofNullable(this.apiProperties.getAws())
                                      .map(ApiProperties.Aws::getBucketName)
                                      .filter(bName -> !bName.isEmpty());
        if (bucket.isPresent()) {
            log.warn("Utilizando bucket {} para armazenamento", bucket.get());
            return new AwsS3StorageService(this.apiProperties.getAws()
                                                             .toStaticCredentialsProvider(),
                bucket.get());
        } else {
            log.warn("Utilizando diretório {} do FS para armazenamento", this.apiProperties.getAppHome());
            return new FsStorageService(Paths.get(this.apiProperties.getAppHome()));
        }
    }
}
