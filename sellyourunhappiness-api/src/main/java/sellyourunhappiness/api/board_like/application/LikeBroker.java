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
public class LikeBroker {

    private final BoardLikeService boardLikeService;
    private final UserService userService;
    private final BoardService boardService;

    public void createLike(String email, Long boardId) {
        User user = userService.findByEmail(email);
        Board board = boardService.findById(boardId);

        boardLikeService.createLike(user,board);
    }

    public void createDislike(String email, Long boardId) {
        User user = userService.findByEmail(email);
        Board board = boardService.findById(boardId);
        boardLikeService.createDislike(user,board);
    }



}
