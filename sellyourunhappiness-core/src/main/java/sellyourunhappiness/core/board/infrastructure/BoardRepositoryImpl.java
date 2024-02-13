package sellyourunhappiness.core.board.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board.domain.enums.BoardStatus;
import sellyourunhappiness.core.board.domain.enums.BoardType;
import sellyourunhappiness.core.config.querydsl.QuerydslRepositorySupport;

import static sellyourunhappiness.core.board.domain.QBoard.board;

public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {

    public BoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> findByTypeAndStatusAllDesc(BoardType type, BoardStatus status, Pageable pageable) {
        return applyPagination(pageable,
                contentQuery -> contentQuery
                        .select(board)
                        .from(board)
                        .where(
                                boardTypeEq(type),
                                boardStatusEq(status)
                        )
                , countQuery -> countQuery
                        .select(board.count())
                        .from(board)
                        .where(
                                boardTypeEq(type),
                                boardStatusEq(status)
                        ));
    }

    private BooleanExpression boardTypeEq(BoardType type) {
        return type != null ? board.type.eq(type) : null;
    }

    private BooleanExpression boardStatusEq(BoardStatus status) {
        return status != null ? board.status.eq(status) : null;
    }
}


