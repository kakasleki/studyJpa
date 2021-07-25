package com.kakasleki.edu.jpa.repository;

import com.kakasleki.edu.jpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return this.em.createQuery("select m from Member m")
                .getResultList();
    }
}
