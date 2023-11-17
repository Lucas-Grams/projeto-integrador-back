package br.com.pnipapi.config;

import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Config {
    // CORS
    public static final String ALLOWED_ORIGINS = "*";
    public static final String ALLOWED_HEADERS = "*";

    // TOKEN
    public static final long EXPIRATION = Duration.ofHours(8).toMillis();
    public static final String SIGNATURE = "WoPfe7MJiEMy+CKXeCZB8pss/86pivmdckYOZcYk/00=";
}
