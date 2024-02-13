package sellyourunhappiness.core.board.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import sellyourunhappiness.core.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}
