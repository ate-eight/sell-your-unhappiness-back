package sellyourunhappiness.core.board.domain.enums;

import lombok.Getter;
import sellyourunhappiness.core.config.converter.EnumType;

@Getter
public enum BoardStatus implements EnumType {
    SALE("SALE", "판매중"),
    SOLD_OUT("SOLD_OUT", "품절");

    private String code;
    private String name;

    BoardStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }
}


