package sellyourunhappiness.api.board_comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sellyourunhappiness.api.board_comment.dto.BoardCommentRegisterParam;
import sellyourunhappiness.api.board_comment.dto.BoardCommentResponse;
import sellyourunhappiness.api.board_comment.dto.BoardCommentUpdateParam;
import sellyourunhappiness.core.board_comment.application.BoardCommentService;
import sellyourunhappiness.core.board_comment.domain.BoardComment;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardCommentBroker {

    private final BoardCommentService boardCommentService;

    public String save(BoardCommentRegisterParam param) {
        boardCommentService.save(param.parentId(), param.boardId(), param.content());
        return "success";
    }

    public String update(Long id, BoardCommentUpdateParam param) {
        boardCommentService.update(id, param.content());
        return "success";
    }

    public List<BoardCommentResponse> findAllByBoardId(Long boardId) {
        List<BoardComment> boardComments = boardCommentService.findAllByBoardId(boardId);

        return boardComments.stream()
                .map(BoardCommentResponse::toResponse)
                .collect(Collectors.toList());
    }
}


