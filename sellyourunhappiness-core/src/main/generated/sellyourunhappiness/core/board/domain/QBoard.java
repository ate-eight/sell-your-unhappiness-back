package sellyourunhappiness.core.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 362754542L;

    public static final QBoard board = new QBoard("board");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedTime = createDateTime("modifiedTime", java.time.LocalDateTime.class);

    public final EnumPath<sellyourunhappiness.core.board.domain.enums.BoardStatus> status = createEnum("status", sellyourunhappiness.core.board.domain.enums.BoardStatus.class);

    public final StringPath title = createString("title");

    public final EnumPath<sellyourunhappiness.core.board.domain.enums.BoardType> type = createEnum("type", sellyourunhappiness.core.board.domain.enums.BoardType.class);

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

