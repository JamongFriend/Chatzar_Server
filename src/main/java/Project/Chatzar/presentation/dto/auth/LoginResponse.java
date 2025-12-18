package Project.Chatzar.presentation.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long memberId;
    private String nickname;
}
