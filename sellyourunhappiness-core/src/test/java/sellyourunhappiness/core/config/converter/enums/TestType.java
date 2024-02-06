package sellyourunhappiness.core.config.converter.enums;

import lombok.Getter;
import sellyourunhappiness.core.config.converter.EnumType;

@Getter
public enum TestType implements EnumType {
    TEST("TEST", "테스트1"),
    TEST2("TEST2", "테스트2");

    private String code;
    private String name;

    TestType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
