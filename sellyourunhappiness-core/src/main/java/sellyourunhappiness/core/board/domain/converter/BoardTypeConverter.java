package sellyourunhappiness.core.board.domain.converter;

import jakarta.persistence.Converter;
import sellyourunhappiness.core.board.domain.enums.BoardType;
import sellyourunhappiness.core.config.converter.AbstractEnumAttributeConverter;

@Converter
public class BoardTypeConverter extends AbstractEnumAttributeConverter<BoardType> {
    public static final String enumName = "BoardType";

    public BoardTypeConverter() {
        super(BoardType.class, false, enumName);
    }
}
