package sellyourunhappiness.core.config.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sellyourunhappiness.core.config.converter.enums.TestType;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("컨버터 유틸")
class EnumConverterUtilsTest {

    @Nested
    @DisplayName("컨버터 유틸 ofCode")
    class ofCode {

        @Test
        @DisplayName("ofCode 성공")
        void ofCodeSuccess() {
            //given
            TestType requestType = TestType.TEST;

            //when
            EnumType enumType = EnumConverterUtils.ofCode(requestType.getDeclaringClass(), requestType.getCode());

            //then
            assertThat(enumType).isEqualTo(requestType);
        }

        @Test
        @DisplayName("ofCode 빈 값 Null반환")
        void ofCodeReturnNull() {
            //given, when
            EnumType enumType = EnumConverterUtils.ofCode(TestType.class, "");

            //then
            assertNull(enumType);
        }

        @Test
        @DisplayName("ofCode 예외")
        void ofCodeException() {
            //given
            TestType requestType = TestType.TEST;
            String code = "예외";

            //when, then
            assertThatThrownBy(() -> EnumConverterUtils.ofCode(requestType.getDeclaringClass(), code))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage(String.format("%s : code [%s]가 존재하지 않습니다.",
                            requestType.getDeclaringClass().getSimpleName(), code));
        }
    }

    @Nested
    @DisplayName("컨버터 유틸 ofName")
    class ofName {

        @Test
        @DisplayName("ofName 성공")
        void ofNameSuccess() {
            //given
            TestType requestType = TestType.TEST;

            //when
            EnumType enumType = EnumConverterUtils.ofName(requestType.getDeclaringClass(), requestType.getName());

            //then
            assertThat(enumType).isEqualTo(requestType);
        }

        @Test
        @DisplayName("ofName 빈 값 Null반환")
        void ofNameReturnNull() {
            //given, when
            EnumType enumType = EnumConverterUtils.ofName(TestType.class, "");

            //then
            assertNull(enumType);
        }

        @Test
        @DisplayName("ofName 예외")
        void ofNameException() {
            //given
            TestType requestType = TestType.TEST;
            String name = "예외";

            //when, then
            assertThatThrownBy(() -> EnumConverterUtils.ofName(requestType.getDeclaringClass(), name))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage(String.format("%s : name [%s]가 존재하지 않습니다.",
                            requestType.getDeclaringClass().getSimpleName(), name));
        }
    }

    @Nested
    @DisplayName("컨버터 유틸 toCode")
    class toCode {

        @Test
        @DisplayName("toCode 성공")
        void toCodeSuccess() {
            //given
            TestType requestType = TestType.TEST;

            //when
            String code = EnumConverterUtils.toCode(requestType);

            //then
            assertThat(code).isEqualTo(requestType.getCode());
        }

        @Test
        @DisplayName("toCode null 빈 값 반환")
        void toCodeReturnBlank() {
            //given, when
            String code = EnumConverterUtils.toCode(null);

            //then
            assertThat(code).isBlank();
        }
    }
}


