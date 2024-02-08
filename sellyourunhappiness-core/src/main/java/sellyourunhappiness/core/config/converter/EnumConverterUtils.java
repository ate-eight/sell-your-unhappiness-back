package sellyourunhappiness.core.config.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.EnumSet;
import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumConverterUtils {
    public static <T extends Enum<T> & EnumType> T ofCode(Class<T> enumClass, String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }

        return EnumSet.allOf(enumClass).stream()
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("%s : code [%s]가 존재하지 않습니다.", enumClass.getSimpleName(), code)));
    }

    public static <T extends Enum<T> & EnumType> T ofName(Class<T> enumClass, String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }

        return EnumSet.allOf(enumClass).stream()
                .filter(v -> v.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("%s : name [%s]가 존재하지 않습니다.", enumClass.getSimpleName(), name)));
    }

    public static <T extends Enum<T> & EnumType> String toCode(T enumValue) {
        if (enumValue == null) {
            return "";
        }
        return enumValue.getCode();
    }
}


