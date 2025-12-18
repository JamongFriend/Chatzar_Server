package Project.Chatzar.presentation.dto.auth;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}
