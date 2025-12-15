package Project.Chatzar.presentation.dto;


import Project.Chatzar.Domain.member.MemberStatus;
import Project.Chatzar.Domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String age;
    private MemberStatus status;

    public static MemberResponse fromEntity(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getNickname(),
                member.getAge(),
                member.getStatus()
        );
    }
}
