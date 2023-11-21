package br.com.pnipapi;

import br.com.pnipapi.config.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(ApiProperties.class)
public class PnipApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PnipApiApplication.class, args);
	}

}
