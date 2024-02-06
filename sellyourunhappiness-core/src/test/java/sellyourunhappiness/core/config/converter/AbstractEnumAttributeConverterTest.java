package sellyourunhappiness.core.config.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sellyourunhappiness.core.config.converter.enums.TestType;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Nested
@DisplayName("추상화 컨버터")
class AbstractEnumAttributeConverterTest {

    @Nested
    @DisplayName("toDatabase")
    class convertToDatabaseColumn {

        @Test
        @DisplayName("toDatabase null 예외처리")
        void toDatabaseNullException() {
            //given
            String enumName = "TestType";

            AbstractEnumAttributeConverter<TestType> converter =
                    new AbstractEnumAttributeConverter<>(TestType.class, false, enumName);


            //when, then
            assertThatThrownBy(() -> converter.convertToDatabaseColumn(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(String.format("%s(은)는 NULL로 저장할 수 없습니다.", enumName));
        }
    }

    @Nested
    @DisplayName("toEntityAttribute")
    class convertToEntityAttribute {

        @Test
        @DisplayName("toEntityAttribute null 예외처리")
        void ofCodeSuccess() {
            //given
            String enumName = "TestType";

            AbstractEnumAttributeConverter<TestType> converter =
                    new AbstractEnumAttributeConverter<>(TestType.class, false, enumName);

            //when, then
            assertThatThrownBy(() -> converter.convertToEntityAttribute(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(String.format("%s(이)가 DB에 NULL 혹은 Empty로(%s) 저장되어 있습니다.", enumName, null));
        }
    }
}