package sellyourunhappiness.core.board_comment.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardComment is a Querydsl query type for BoardComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardComment extends EntityPathBase<BoardComment> {

    private static final long serialVersionUID = 1599083953L;

    public static final QBoardComment boardComment = new QBoardComment("boardComment");

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public QBoardComment(String variable) {
        super(BoardComment.class, forVariable(variable));
    }

    public QBoardComment(Path<? extends BoardComment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardComment(PathMetadata metadata) {
        super(BoardComment.class, metadata);
    }

}

