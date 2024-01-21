package sellyourunhappiness.api.member.dto;

import sellyourunhappiness.core.member.domain.enums.MemberType;

public record MemberResisterParam(
        String name,
        String nickname,
        MemberType grade
) {

}
