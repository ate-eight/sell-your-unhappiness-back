package sellyourunhappiness.api.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sellyourunhappiness.api.member.dto.MemberResisterParam;

import sellyourunhappiness.core.member.application.MemberService;
import sellyourunhappiness.core.member.domain.Member;

@Service
@RequiredArgsConstructor
public class MemberBroker {

    private final MemberService memberService;

    public Member save(MemberResisterParam param) {
        return memberService.save(param.name(), param.nickname(), param.grade());
    }

}
