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
    public Member saveTest(String name, String nickname) {
        Member member = Member.create(name, nickname, MemberType.one);
        return memberRepository.save(member);
    }
}
