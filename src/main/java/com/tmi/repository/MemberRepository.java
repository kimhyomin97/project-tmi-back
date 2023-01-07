package com.tmi.repository;

import com.tmi.dto.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberid(String memberId);

    boolean existsByMemberidAndMemberpw(String memberId, String memberPw);

//    @Query(value = "SELECT * FROM member WHERE ")

}
