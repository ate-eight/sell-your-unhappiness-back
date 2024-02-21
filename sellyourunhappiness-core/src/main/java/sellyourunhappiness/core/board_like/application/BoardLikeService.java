package sellyourunhappiness.core.board_like.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board_like.domain.BoardLike;
import sellyourunhappiness.core.board_like.domain.enums.LikeType;
import sellyourunhappiness.core.board_like.infrastructure.BoardLikeRepository;
import sellyourunhappiness.core.user.domain.User;

<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Optional;

=======
>>>>>>> 354a831 (feat : 좋아요 기능 구현)
=======
import java.util.Optional;

>>>>>>> e201dad (bug : AOP로 인해 common이 null로 되는 현상)
@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;

<<<<<<< HEAD
<<<<<<< HEAD
    public void save(Long boardId, Long userId, LikeType type) {
        boardLikeRepository.save(BoardLike.create(userId, boardId, type));
    }

    public Optional<BoardLike> findByBoardIdAndUserIdAndLikeType(Long boardId, Long userId, LikeType type) {
        return boardLikeRepository.findByBoardIdAndUserIdAndLikeType(boardId, userId, type);
    }
=======
    // 제 생각 Service 테스트할때 1이라도 오게?해야되나 // 윤찬님은 void // 민규 void 안좋다! createLike 지금이야 return값이 없어도되는데
    // 테이블에 컬럼한개를 더팠는데 만약에 그걸 반환해줘야하는데 void로 되있으면 당연히 수정을 하게 됨
    // CQRS 명령과 쿼리를 분리해야   //
    // 객체지향을 쓰니까 지금은 문제가없지만 다른곳에서 쓰면 문제가 생길까?가 중요하다.
    public void createLike(User user, Board board) {
        boardLikeRepository.findByBoardIdAndUserIdAndLikeType(board.getId(), user.getId(), LikeType.LIKE)
                .ifPresent(boardLike -> {
                    throw new IllegalArgumentException("보드라이크가 이미 존재합니다.");
                });

        boardLikeRepository.save(BoardLike.create(user, board, LikeType.LIKE));

=======
    public void save(Long boardId, Long userId, LikeType type) {
        boardLikeRepository.save(BoardLike.create(userId, boardId, type));
>>>>>>> e201dad (bug : AOP로 인해 common이 null로 되는 현상)
    }

    public Optional<BoardLike> findByBoardIdAndUserIdAndLikeType(Long boardId, Long userId, LikeType type) {
        return boardLikeRepository.findByBoardIdAndUserIdAndLikeType(boardId, userId, type);
    }
<<<<<<< HEAD

>>>>>>> 354a831 (feat : 좋아요 기능 구현)
=======
>>>>>>> e201dad (bug : AOP로 인해 common이 null로 되는 현상)
}
