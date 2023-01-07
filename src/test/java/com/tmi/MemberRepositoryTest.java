package com.tmi;

import com.tmi.dto.Member;
import com.tmi.repository.MemberRepository;
//import org.h2.util.TempFileDeleter;
import com.tmi.service.MemberService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest // H2 DB를 자동으로 실행해준다
@WebAppConfiguration
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @AfterAll // junit에서 단위 테스트가 끝날 때마다 수행되는 메소드
    // 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용한다
    // 여러 테스트가 동시에 수행되면 테스트용 DB인 H2에 데이터가 그대로 남아 있어 다음 테스트가 실패할 수 있다.
    public void cleanup(){
//        memberRepository.deleteAll();
    }

    @Test
    public void crud_test() throws Exception {
        Member test_member1 = new Member("12345", "1234");
        memberService.signUp(test_member1);
        memberService.login(test_member1);

        Member test_member2 = new Member("1234", "1234");
        memberService.signUp(test_member2);
        memberService.login(test_member2);

    }

//    @Test
//    public void save_road(){
//        String test_id = "테스트 아이디";
//        String test_pw = "테스트 비밀번호";
////        String test_email = "테스트 이메일";
//
//        memberRepository.save(Member.builder() // 테이블 ...에 insert/update 쿼리를 실행
//                .memberid(test_id) // idx값이 있다면 update, 없다면 insert 쿼리가 실행된다.
//                .memberpw(test_pw)
////                .email(test_email)
//                .build());
//
//        // when
//        List<Member> memberList = memberRepository.findAll(); // 테이블에 있는 모든 데이터를 조회해오는 메소드
//
//        // then
//        Member member = memberList.get(0);
//        assertThat(member.getMemberid()).isEqualTo(test_id);
//        assertThat(member.getMemberpw()).isEqualTo(test_pw);
////        assertThat(member.getEmail()).isEqualTo(test_email);
//    } // https://velog.io/@swchoi0329/Spring-Boot%EC%97%90%EC%84%9C-JPA-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
}
