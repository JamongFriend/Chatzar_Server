package Project.Chatzar.presentation.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record ReissueRequest(
        @NotBlank String refreshToken
) {}
