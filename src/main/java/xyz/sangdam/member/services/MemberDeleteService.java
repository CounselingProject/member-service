package xyz.sangdam.member.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.sangdam.member.entities.Member;
import xyz.sangdam.member.repositories.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberDeleteService {
    private final MemberRepository memberRepository;

    public Member deleteMember(Long seq) {
        Member member = memberRepository.findById(seq).orElse(null);
        if (member != null) {

            memberRepository.delete(member);
            memberRepository.flush();
            return member;
        }
        return null;
    }
}
