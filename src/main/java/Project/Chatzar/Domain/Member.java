package Project.Chatzar.Domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    // 회원 개인정보
    private String name;
    private String email;
    private String password;

    // 세부정보
    private String nickname;
    private String age;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    protected Member(){}

    public Member(Long id, String name, String email, String password, String nickname, String age, MemberStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.status = status;
    }
}
