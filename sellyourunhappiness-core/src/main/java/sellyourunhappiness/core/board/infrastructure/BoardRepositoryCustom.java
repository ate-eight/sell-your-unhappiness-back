package sellyourunhappiness.core.board.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board.domain.enums.BoardStatus;
import sellyourunhappiness.core.board.domain.enums.BoardType;

public interface BoardRepositoryCustom {
    Page<Board> findByTypeAndStatusAllDesc(BoardType type, BoardStatus status, Pageable pageable);
}
