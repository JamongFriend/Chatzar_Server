package Project.Chatzar.Domain.member;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    // 회원 개인정보
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // 세부정보
    @Column(unique = true, nullable = false)
    private String nickname;

    private Long age;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    protected Member(){}

    public Member(String name, String email, String password, String nickname, Long age, MemberStatus status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.status = status;
    }
}
