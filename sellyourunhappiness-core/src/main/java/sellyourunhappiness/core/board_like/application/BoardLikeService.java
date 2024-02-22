package sellyourunhappiness.core.board_like.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board_like.domain.BoardLike;
import sellyourunhappiness.core.board_like.domain.enums.LikeType;
import sellyourunhappiness.core.board_like.infrastructure.BoardLikeRepository;
import sellyourunhappiness.core.user.domain.User;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;


    public void save(Long boardId, Long userId, LikeType type) {
        boardLikeRepository.save(BoardLike.create(userId, boardId, type));
    }

    public Optional<BoardLike> findByBoardIdAndUserIdAndLikeType(Long boardId, Long userId, LikeType type) {
        return boardLikeRepository.findByBoardIdAndUserIdAndLikeType(boardId, userId, type);
    }

}
