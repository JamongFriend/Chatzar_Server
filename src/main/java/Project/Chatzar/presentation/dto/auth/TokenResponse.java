package Project.Chatzar.presentation.dto.auth;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken
) {}
