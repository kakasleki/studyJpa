package com.kakasleki.edu.jpa.repository;

import com.kakasleki.edu.jpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        this.em.persist(member);
        return member;
    }

    public void delete(Member member) {
        this.em.remove(member);
    }

    public List<Member> findAll() {
        return this.em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = this.em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Long count() {
        return this.em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return this.em.find(Member.class, id);
    }

    public int bulkAgePlus(int age) {
        return this.em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();

    }
}
