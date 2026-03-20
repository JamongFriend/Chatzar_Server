package Project.Chatzar.presentation.dto.member;

import lombok.Builder;

@Builder
public record SignupResponse(
        Long memberId,
        String email,
        String nickname
) {}
