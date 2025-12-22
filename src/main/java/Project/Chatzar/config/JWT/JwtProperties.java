package Project.Chatzar.config.JWT;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        long accessExpSeconds,
        long refreshExpDays,
        String issuer
) {}
