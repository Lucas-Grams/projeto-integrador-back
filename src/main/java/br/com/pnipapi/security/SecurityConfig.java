package br.com.pnipapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors();
        // TODO: habilitar apos implementar autenticacao
        /*
        http.authorizeHttpRequests(httpReq -> {
            httpReq.requestMatchers()
                    .hasAnyAuthority("admin")
                    .anyRequest()
                    .authenticated();
        });
        */
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //http.oauth2ResourceServer(jwt -> jwt.jwt(j -> j.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())));
        return http.build();
    }
}

