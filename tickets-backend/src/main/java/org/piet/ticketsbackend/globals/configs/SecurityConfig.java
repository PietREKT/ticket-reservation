package org.piet.ticketsbackend.globals.configs;

import org.piet.ticketsbackend.globals.exceptions.handlers.FilterExceptionHandler;
import org.piet.ticketsbackend.globals.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Value("${app.api.prefix}")
    String apiPrefix;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter, FilterExceptionHandler filterExceptionHandler){
        return http
                .authorizeHttpRequests(req ->
                        req.anyRequest()
                                .permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterExceptionHandler, JwtFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    CorsConfigurationSource cors(){
//        var corsConfig = new CorsConfiguration();
//        corsConfig.addAllowedMethod("*");
//        corsConfig.
//    }
}
