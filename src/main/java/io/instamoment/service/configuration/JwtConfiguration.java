package io.instamoment.service.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {
    private String securityKey;
    private String expiresIn;
    private String prefix;
    private String header;
}
