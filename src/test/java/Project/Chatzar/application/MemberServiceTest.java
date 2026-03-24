package Project.Chatzar.application;

import Project.Chatzar.Domain.member.Member;
import Project.Chatzar.Domain.member.MemberRepository;
import Project.Chatzar.presentation.dto.member.JoinRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("동일한 닉네임으로 가입해도 서로 다른 고유 태그가 할당되어야 한다")
    void join_with_same_nickname_different_tag() {
        String nickname = "피카츄";
        JoinRequest user1 = new JoinRequest("사용자1", "user1@test.com", "pass123!", nickname, 27L);
        JoinRequest user2 = new JoinRequest("사용자2", "user2@test.com", "pass123!", nickname, 25L);

        Long id1 = memberService.join(user1).memberId();
        Long id2 = memberService.join(user2).memberId();

        Member member1 = memberRepository.findById(id1).orElseThrow();
        Member member2 = memberRepository.findById(id2).orElseThrow();

        assertThat(member1.getNickname()).isEqualTo(nickname);
        assertThat(member2.getNickname()).isEqualTo(nickname);

        assertThat(member1.getTag()).isNotNull();
        assertThat(member2.getTag()).isNotNull();
        assertThat(member1.getTag()).isNotEqualTo(member2.getTag());

        System.out.println("User1: " + member1.getNickname() + "#" + member1.getTag());
        System.out.println("User2: " + member2.getNickname() + "#" + member2.getTag());
    }

    @Test
    @DisplayName("회원가입 시 비밀번호가 암호화되는지 테스트")
    void signUp_success() {
        JoinRequest request = new JoinRequest("홍길동", "test@test.com", "password123", "길동이", 27L);

        Long savedId = memberService.join(request).memberId();

        Member member = memberRepository.findById(savedId).orElseThrow();
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(passwordEncoder.matches("password123", member.getPassword())).isTrue();
    }

}