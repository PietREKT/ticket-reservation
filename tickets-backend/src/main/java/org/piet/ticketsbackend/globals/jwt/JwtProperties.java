package org.piet.ticketsbackend.globals.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtProperties {
    private String issuer;
    private String secret;
    private Long expiration;
}
