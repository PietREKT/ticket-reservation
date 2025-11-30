package org.piet.ticketsbackend;

import org.piet.ticketsbackend.globals.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class TicketsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketsBackendApplication.class, args);
    }

}
