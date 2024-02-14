package sellyourunhappiness.core.board_like.domain.converter;

import jakarta.persistence.Converter;
import sellyourunhappiness.core.board_like.domain.enums.LikeType;
import sellyourunhappiness.core.config.converter.AbstractEnumAttributeConverter;

@Converter
public class LikeTypeConverter extends AbstractEnumAttributeConverter<LikeType> {

    public static final String enumName = "LikeType";

    public LikeTypeConverter() {
        super(LikeType.class, false, enumName);
    }
}
