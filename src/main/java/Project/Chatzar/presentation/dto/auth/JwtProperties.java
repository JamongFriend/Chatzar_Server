package Project.Chatzar.presentation.dto.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        String issuer,
        long accessTokenExpMinutes,
        long refreshTokenExpDays
) {}
