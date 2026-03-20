package Project.Chatzar.presentation.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JoinRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String nickname,
        @NotNull Long age
) {}
