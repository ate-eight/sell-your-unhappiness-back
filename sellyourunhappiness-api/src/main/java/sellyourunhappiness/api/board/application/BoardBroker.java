package sellyourunhappiness.api.board.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sellyourunhappiness.api.board.dto.BoardRegisterParam;
import sellyourunhappiness.api.board.dto.BoardResponse;
import sellyourunhappiness.api.board.dto.BoardSearchCondition;
import sellyourunhappiness.api.board.dto.BoardSearchResponse;
import sellyourunhappiness.api.config.page.PageResponse;
import sellyourunhappiness.core.board.application.BoardService;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.config.page.PageModel;

@Service
@RequiredArgsConstructor
public class BoardBroker {

    private final BoardService boardService;

    public String save(BoardRegisterParam param) {
        boardService.save(param.type(), param.title(), param.content());
        return "success";
    }

    public BoardResponse findById(Long id) {
        Board board = boardService.findById(id);
        return BoardResponse.toResponse(board);
    }

    public PageResponse<BoardSearchResponse> findByTypeAndStatusAllDesc(BoardSearchCondition condition) {
        PageModel<Board> model =
                boardService.findByTypeAndStatusAllDesc(condition.type(), condition.status(), condition.page());

        List<BoardSearchResponse> boardSearchResponses = model.getContents().stream()
                .map(BoardSearchResponse::toResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(model, boardSearchResponses);
    }
}


