package com.tmi.service;

import com.tmi.dto.Member;

public interface MemberService {

    // member create : 회원가입
    public boolean signUp(Member member) throws Exception;

    // member read : 로그인
    public boolean login(Member member) throws Exception;

    // member update : 정보 수정
    public Member updateMember(Member member) throws Exception;

    // member delete : 회원 탈퇴
    public Member deleteMember(Member member) throws Exception;

}
