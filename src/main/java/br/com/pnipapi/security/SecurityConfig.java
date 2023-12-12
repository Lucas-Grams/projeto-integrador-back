package br.com.pnipapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        http.authorizeHttpRequests(httpReq -> {
            httpReq.requestMatchers()
                    .hasAnyAuthority("admin")
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                    .anyRequest()
                    .permitAll();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //http.oauth2ResourceServer(jwt -> jwt.jwt(j -> j.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())));
        return http.build();
    }
}

