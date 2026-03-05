package Project.Chatzar.Domain.member;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_nickname_tag", columnNames = {"nickname", "tag"})
})
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
    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, updatable = false, length = 4)
    private String tag;

    private Long age;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    protected Member(){}

    public Member(String name, String email, String password, String nickname, String tag, Long age, MemberStatus status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.tag = tag;
        this.age = age;
        this.status = status;
    }

    public static Member createWithTag(String name, String email, String password, String nickname, String tag, Long age) {
        return new Member(name, email, password, nickname, tag, age, MemberStatus.ACTIVE);
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
