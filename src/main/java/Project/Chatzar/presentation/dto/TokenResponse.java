package Project.Chatzar.presentation.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}
