package sellyourunhappiness.core.board_like.domain.enums;

import lombok.Getter;
import sellyourunhappiness.core.config.converter.EnumType;

@Getter
public enum LikeType implements EnumType {

    LIKE("LIKE","좋아요"),
    DISLIKE("DISLIKE","싫어요")
    ;

    private String code;
    private String name;

    LikeType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
