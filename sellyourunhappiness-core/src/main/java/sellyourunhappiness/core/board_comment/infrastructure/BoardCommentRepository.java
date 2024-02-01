package sellyourunhappiness.core.board_comment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import sellyourunhappiness.core.board_comment.domain.BoardComment;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    List<BoardComment> findAllByBoardId(Long boardId);
}
