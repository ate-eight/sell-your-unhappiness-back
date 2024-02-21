package sellyourunhappiness.api.board_like.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sellyourunhappiness.core.board.application.BoardService;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board_like.application.BoardLikeService;
import sellyourunhappiness.core.board_like.domain.enums.LikeType;
import sellyourunhappiness.core.user.application.UserService;
import sellyourunhappiness.core.user.domain.User;

@Service
@RequiredArgsConstructor
public class BoardLikeBroker {

    private final BoardLikeService boardLikeService;
    private final UserService userService;
    private final BoardService boardService;

    public String like(String email, Long boardId, String type) {
        User user = userService.findByEmail(email);
        Board board = boardService.findById(boardId);

        LikeType likeType = type.equals("like") ? LikeType.LIKE : LikeType.DISLIKE;

        boardLikeService.findByBoardIdAndUserIdAndLikeType(board.getId(), user.getId(), likeType)
                .ifPresent(boardLike -> {
                    throw new IllegalArgumentException("해당 데이터가 존재합니다");
                });

        boardLikeService.save(board.getId(), user.getId(), likeType);
        return "success";
    }
}


