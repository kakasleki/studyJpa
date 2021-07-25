package com.kakasleki.edu.jpa.repository;

import com.kakasleki.edu.jpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
