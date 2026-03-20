package Project.Chatzar.presentation.dto.auth.response;

import lombok.Builder;

@Builder
public record TokenResponse(
        Long memberId,
        String accessToken,
        String refreshToken
) {}
