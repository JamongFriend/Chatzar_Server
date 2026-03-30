package Project.Chatzar.presentation.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2~10자여야 합니다.")
    String nickname
) {
}
