package sellyourunhappiness.core.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sellyourunhappiness.core.member.domain.Member;
import sellyourunhappiness.core.member.domain.enums.MemberType;
import sellyourunhappiness.core.member.infrastructure.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member save(String name, String nickname, MemberType grade) {
        Member member = Member.create("test", nickname, grade);
        return memberRepository.save(member);
    }
}
