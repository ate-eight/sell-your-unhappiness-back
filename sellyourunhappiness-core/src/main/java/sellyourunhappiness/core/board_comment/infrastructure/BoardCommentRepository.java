package sellyourunhappiness.core.board_comment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import sellyourunhappiness.core.board_comment.domain.BoardComment;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    List<BoardComment> findAllByBoardId(Long boardId);

    Optional<BoardComment> findByParentIdAndBoardId(Long parentId, Long boardId);
}
