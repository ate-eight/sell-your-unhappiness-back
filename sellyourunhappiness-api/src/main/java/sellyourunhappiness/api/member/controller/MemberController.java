package sellyourunhappiness.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sellyourunhappiness.api.member.application.MemberBroker;
import sellyourunhappiness.api.member.dto.MemberResisterParam;
import sellyourunhappiness.core.member.domain.Member;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberBroker memberBroker;
    @PostMapping("/v1/member")
    public ResponseEntity<Member> create(@RequestBody MemberResisterParam param) {
        return ResponseEntity.ok(memberBroker.save(param));
    }

}
