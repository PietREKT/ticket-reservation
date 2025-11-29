package org.piet.ticketsbackend.globals.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Value("${app.api.prefix}")
    String apiPrefix;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .authorizeHttpRequests(req ->
                        req.anyRequest()
                                .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

//    @Bean
//    CorsConfigurationSource cors(){
//        var corsConfig = new CorsConfiguration();
//        corsConfig.addAllowedMethod("*");
//        corsConfig.
//    }
}
