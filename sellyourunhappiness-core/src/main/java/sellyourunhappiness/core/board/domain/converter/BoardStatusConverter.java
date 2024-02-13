package sellyourunhappiness.core.board.domain.converter;

import jakarta.persistence.Converter;
import sellyourunhappiness.core.board.domain.enums.BoardStatus;
import sellyourunhappiness.core.config.converter.AbstractEnumAttributeConverter;

@Converter
public class BoardStatusConverter  extends AbstractEnumAttributeConverter<BoardStatus> {
    public static final String enumName = "BoardStatus";

    public BoardStatusConverter() {
        super(BoardStatus.class, false, enumName);
    }
}
