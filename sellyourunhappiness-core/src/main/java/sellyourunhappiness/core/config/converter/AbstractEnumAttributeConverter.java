package sellyourunhappiness.core.config.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.util.StringUtils;

public class AbstractEnumAttributeConverter<E extends Enum<E> & EnumType> implements AttributeConverter<E, String> {
    private Class<E> targetEnumClass;

    private boolean nullable;

    private String enumName;

    @Override
    public String convertToDatabaseColumn(E attribute) {
        if (!nullable && attribute == null) {
            throw new IllegalArgumentException(String.format("%s(은)는 NULL로 저장할 수 없습니다.", enumName));
        }
        return EnumConverterUtils.toCode(attribute);
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (!nullable && !StringUtils.hasText(dbData)) {
            throw new IllegalArgumentException(String.format("%s(이)가 DB에 NULL 혹은 Empty로(%s) 저장되어 있습니다.", enumName, dbData));
        }
        return EnumConverterUtils.ofCode(targetEnumClass, dbData);
    }

    public AbstractEnumAttributeConverter(Class<E> targetEnumClass, boolean nullable, String enumName) {
        this.targetEnumClass = targetEnumClass;
        this.nullable = nullable;
        this.enumName = enumName;
    }
}