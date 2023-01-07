package com.tmi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter // Entity 클래스에서는 절대 Setter 메소드를 만들지 않는다 -> 클래스의 인스턴스 값들이 언제 변경되는지 명확하게 알 수 없다.
// Entity의 Setter를 생성하지 않기 위해서 로그인 전용 DTO를 따로 생성하는 방향 고려
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@Entity // 테이블과 링크될 클래스임을 나타낸다
// 기본값으로 카멜케이스 이름을 언더 스키어 네이밍으로 매칭
// ex) UserService.java -> user_service table
@Table(name = "member")
public class Member {

    @Id // PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성 규칙, IDENTITY : auto_increment
    private Long idx;

    @Column(length = 45, nullable = false) // 필수는 아님
    private String memberid;

    @Column(length = 45, nullable = false)
    private String memberpw;

    @Column(length = 45, nullable = false)
    private String salt;

//    @Column(length = 45, nullable = false)
//    private String email;

    @Builder // 해당 클래스의 빌더 패턴 클래스를 생성
    public Member(String memberid, String memberpw){
        this.memberid = memberid;
        this.memberpw = memberpw;
//        this.email = email;
    }
}
