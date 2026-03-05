package Project.Chatzar.presentation.dto.member;


import Project.Chatzar.Domain.member.MemberStatus;
import Project.Chatzar.Domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;


public record MemberResponse(
        Long id,
        String name,
        String email,
        String nickname,
        String tag,
        Long age,
        MemberStatus status
) {
    public static MemberResponse fromEntity(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getNickname(),
                member.getTag(),
                member.getAge(),
                member.getStatus()
        );
    }
}
