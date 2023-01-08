package com.tmi.service;

import com.tmi.dto.Member;
import com.tmi.repository.MemberRepository;
import com.tmi.util.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    Encrypt encrypt;

//    회원가입
    @Override
    public boolean signUp(Member member) throws Exception {
        // salt 값을 랜덤으로 생성해서 sha-256 알고리즘으로 비밀번호 암호화 저장하는 로직 추가
        if(memberRepository.existsByMemberid(member.getMemberid()) == true){
            // 아이디가 이미 존재하는 경우
            return false;
        }
        else {
            // 아이디가 존재하지 않으면, DB에 추가

//            encrypt util 적용 예시
//            String salt = encrypt.generateSalt();
            byte[] salt = encrypt.generateSalt();
            String encryptPassword = encrypt.encryptPassword(member.getMemberpw(), salt);
            // salt value 생성과 암호화 작업을 분리

            // salt value 저장
            member.setSalt(salt);
            // 암호화 이후 비밀번호 저장
            member.setMemberpw(encryptPassword);

            System.out.println(member.getMemberid());
            System.out.println(member.getMemberpw());
            System.out.println(member.getSalt());
            System.out.println(member.getIdx());

            memberRepository.save(member); // 회원 정보 저장
            // 저장 성공하면 return true, 실패하면 return false 하는게 더 좋은 방향
            return true;
        }
//        return memberRepository.existsByMemberid(member.getMemberid());
    }

    @Override
    public boolean login(Member member) throws Exception {
        // 입력한 비밀번호를 테이블에 저장된 솔트값을 읽어와서 sha-256 알고리즘으로 암호화하여 유효성 검사를 하는 로직 추가
        // 유효성 검사는 추가 검색 필요
        Member guest = memberRepository.findByMemberid(member.getMemberid());
//        System.out.println(guest.toString());

        String encryptPassword = encrypt.encryptPassword(member.getMemberpw(), guest.getSalt());

        // 로그인시 토큰값을 넘겨주는 방향 검토
        if(encryptPassword.equals(member.getMemberpw())){
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public Member updateMember(Member member) throws Exception {
        // 회원정보 수정하는 부분
        return null;
    }

    @Override
    public Member deleteMember(Member member) throws Exception {
        // 회원정보 삭제 로직 -> 완료시 로그인까지 연결되야함
        return null;
    }
}
