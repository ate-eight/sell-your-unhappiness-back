package sellyourunhappiness.core.board.domain.enums;

import lombok.Getter;
import sellyourunhappiness.core.config.converter.EnumType;

@Getter
public enum BoardType implements EnumType {
    COMPANY("COMPANY", "회사"),
    SCHOOL("SCHOOL", "학교"),
    FRIENDS("FRIENDS", "친구"),
    PARENTS("PARENTS", "부모님"),
    WORLD("WORLD", "세상");

    private String code;
    private String name;

    BoardType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}


