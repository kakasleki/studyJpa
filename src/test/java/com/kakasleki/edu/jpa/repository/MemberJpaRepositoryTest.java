package com.kakasleki.edu.jpa.repository;

import com.kakasleki.edu.jpa.entity.Member;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = this.memberJpaRepository.save(member);

        Member findMember = this.memberJpaRepository.find(saveMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        this.memberJpaRepository.save(member1);
        this.memberJpaRepository.save(member2);

        // 단건조회 검증
        Member findMember1 = this.memberJpaRepository.find(member1.getId());
        Member findMember2 = this.memberJpaRepository.find(member2.getId());
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트조회 검증
        List<Member> all = this.memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        Long count = this.memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        this.memberJpaRepository.delete(member1);
        this.memberJpaRepository.delete(member2);

        Long deletedCount = this.memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void bulkUpdate() {
        this.memberJpaRepository.save(new Member("member1", 10));
        this.memberJpaRepository.save(new Member("member2", 19));
        this.memberJpaRepository.save(new Member("member3", 20));
        this.memberJpaRepository.save(new Member("member4", 21));
        this.memberJpaRepository.save(new Member("member5", 40));

        int resultCnt = this.memberJpaRepository.bulkAgePlus(20);
        assertThat(resultCnt).isEqualTo(3);
    }
}
