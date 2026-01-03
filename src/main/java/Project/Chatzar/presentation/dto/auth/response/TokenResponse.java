package Project.Chatzar.presentation.dto.auth.response;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken
) {}
