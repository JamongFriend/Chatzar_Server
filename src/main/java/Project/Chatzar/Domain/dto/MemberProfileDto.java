package Project.Chatzar.Domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberProfileDto {
    private Long id;
    private String email;
    private String nickname;
}
