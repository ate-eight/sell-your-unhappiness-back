package sellyourunhappiness.core.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sellyourunhappiness.core.board.domain.converter.BoardTypeConverter;
import sellyourunhappiness.core.board.domain.enums.BoardStatus;
import sellyourunhappiness.core.board.domain.converter.BoardStatusConverter;
import sellyourunhappiness.core.board.domain.enums.BoardType;

import java.time.LocalDateTime;

import static sellyourunhappiness.core.config.converter.EnumConverterUtils.ofName;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Convert(converter = BoardTypeConverter.class)
    @Column(columnDefinition = "varchar(20)")
    private BoardType type;

    @Convert(converter = BoardStatusConverter.class)
    @Column(columnDefinition = "varchar(20)")
    private BoardStatus status;

    // private Long userId;

    private String title;

    private String content;

    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime modifiedTime = LocalDateTime.now();

    public Board(String type, String status, String title, String content) {
        this.type = ofName(BoardType.class, type);
        this.status = ofName(BoardStatus.class, status);
        this.title = title;
        this.content = content;
    }

    public static Board create(String type, String title, String content) {
        return new Board(type, BoardStatus.SALE.getName(), title, content);
    }
}


