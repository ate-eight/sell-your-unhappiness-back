package sellyourunhappiness.core.board_like.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board_like.domain.BoardLike;
import sellyourunhappiness.core.board_like.domain.enums.LikeType;
import sellyourunhappiness.core.user.domain.User;

public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {

    Optional<BoardLike> findByBoardIdAndUserIdAndLikeType(Long boardId, Long userId, LikeType likeType);

}
