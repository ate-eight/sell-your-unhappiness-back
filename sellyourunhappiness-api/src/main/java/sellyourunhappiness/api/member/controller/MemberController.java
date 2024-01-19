package sellyourunhappiness.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sellyourunhappiness.api.member.application.MemberBroker;
import sellyourunhappiness.api.member.dto.MemberResisterParam;
import sellyourunhappiness.core.member.domain.Member;
import sellyourunhappiness.util.slack.SlackNotification;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberBroker memberBroker;
    @GetMapping("/v1/member")
    public ResponseEntity<Member> createMember(@RequestBody MemberResisterParam param) {
        System.out.println("asdfasf");
        return ResponseEntity.ok(memberBroker.save(param));
    }

}
