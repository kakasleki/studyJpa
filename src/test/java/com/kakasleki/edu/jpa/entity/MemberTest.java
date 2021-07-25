package com.kakasleki.edu.jpa.entity;

import com.kakasleki.edu.jpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        this.em.persist(teamA);
        this.em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        this.em.persist(member1);
        this.em.persist(member2);
        this.em.persist(member3);
        this.em.persist(member4);

        // 초기화
        this.em.flush();
        this.em.clear();

        // 확인
        List<Member> members = this.em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

    @Test
    public void jpaEventBaseEntity() throws InterruptedException {
        Member member = new Member("member1");
        this.memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        this.em.flush();
        this.em.clear();

        Member result = this.memberRepository.findById(member.getId()).get();

        System.out.println("result.getCreateDate = " + result.getCreatedDate());
        System.out.println("result.getCreatedBy = " + result.getCreatedBy());
        System.out.println("result.getLastModifiedDate = " + result.getLastModifiedDate());
        System.out.println("result.getLastModifiedBy = " + result.getLastModifiedBy());
    }
}
