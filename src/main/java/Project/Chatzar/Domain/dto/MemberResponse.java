package Project.Chatzar.Domain.dto;


import Project.Chatzar.Domain.MemberStatus;
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

    public static MemberResponse fromEntity(Project.Chatzar.Domain.Member member) {
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