package com.kakasleki.edu.jpa.controller;

import com.kakasleki.edu.jpa.dto.MemberDto;
import com.kakasleki.edu.jpa.entity.Member;
import com.kakasleki.edu.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/member/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = this.memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/member2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        return this.memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    //@PostConstruct
    public void init() {
        for(int i = 0; i < 100; i++) {
            this.memberRepository.save(new Member("user" + i, i));
        }
    }
}
