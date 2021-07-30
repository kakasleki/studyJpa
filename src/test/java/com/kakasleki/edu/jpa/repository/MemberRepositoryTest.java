package com.kakasleki.edu.jpa.repository;

import com.kakasleki.edu.jpa.dto.MemberDto;
import com.kakasleki.edu.jpa.entity.Member;
import static org.assertj.core.api.Assertions.*;

import com.kakasleki.edu.jpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = this.memberRepository.save(member);

        Member findMember = this.memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        this.memberRepository.save(member1);
        this.memberRepository.save(member2);

        // 단건조회 검증
        Member findMember1 = this.memberRepository.findById(member1.getId()).get();
        Member findMember2 = this.memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트조회 검증
        List<Member> all = this.memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        Long count = this.memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        this.memberRepository.delete(member1);
        this.memberRepository.delete(member2);

        Long deletedCount = this.memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        this.memberRepository.save(m1);
        this.memberRepository.save(m2);

        List<Member> result = this.memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void testFindByUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        this.memberRepository.save(m1);
        this.memberRepository.save(m2);

        List<String> result = this.memberRepository.findByUsernameList();
        assertThat(result.get(0)).isEqualTo(m1.getUsername());
    }

    @Test
    public void testFindByMemberDto() {
        Member m1 = new Member("AAA", 10);
        this.memberRepository.save(m1);

        Team team = new Team("teamA");
        m1.setTeam(team);
        this.teamRepository.save(team);

        List<MemberDto> result = this.memberRepository.findMemberDto();
        for (MemberDto dto : result) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void testFindByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        this.memberRepository.save(m1);
        this.memberRepository.save(m2);

        List<Member> result = this.memberRepository.findByMembers(Arrays.asList(new String[]{"AAA", "BBB"}));
        for (Member m : result) {
            System.out.println("member = " + m.getUsername());
        }
    }

    @Test
    public void paging() {
        this.memberRepository.save(new Member("member1", 10));
        this.memberRepository.save(new Member("member2", 10));
        this.memberRepository.save(new Member("member3", 10));
        this.memberRepository.save(new Member("member4", 10));
        this.memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = this.memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();
        long totalCount = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        this.memberRepository.save(new Member("member1", 10));
        this.memberRepository.save(new Member("member2", 19));
        this.memberRepository.save(new Member("member3", 20));
        this.memberRepository.save(new Member("member4", 21));
        this.memberRepository.save(new Member("member5", 40));

        int resultCnt = this.memberRepository.bulkAgePlus(20);
        //this.em.flush();
        //this.em.clear();
        assertThat(resultCnt).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        this.teamRepository.save(teamA);
        this.teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);

        this.memberRepository.save(member1);
        this.memberRepository.save(member2);

        this.em.flush();
        this.em.clear();

        List<Member> members = this.memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void callCustom() {
        List<Member> result = this.memberRepository.findMemberCustom();
    }

    @Test
    public void queryByExample() {
        Team teamA = new Team("teamA");
        this.em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        this.em.persist(m1);
        this.em.persist(m2);

        this.em.flush();
        this.em.clear();

        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase("age");
        Example<Member> example = Example.of(member, matcher);

        List<Member> result =  this.memberRepository.findAll(example);

        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    public void nativeQuery() {
        Team teamA = new Team("teamA");
        this.em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        this.em.persist(m1);
        this.em.persist(m2);

        this.em.flush();
        this.em.clear();

        Member result = this.memberRepository.findByNativeQuery("m1");
        System.out.println("result = " + result);
    }
}
