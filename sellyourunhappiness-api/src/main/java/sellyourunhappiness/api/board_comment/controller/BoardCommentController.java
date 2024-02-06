package sellyourunhappiness.api.board_comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sellyourunhappiness.api.board_comment.application.BoardCommentBroker;
import sellyourunhappiness.api.board_comment.dto.BoardCommentRegisterParam;
import sellyourunhappiness.api.board_comment.dto.BoardCommentResponse;
import sellyourunhappiness.api.board_comment.dto.BoardCommentUpdateParam;
import sellyourunhappiness.api.config.response.annotation.ApiResponseAnnotation;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sellyourunhappiness.api.config.response.aspect.dto.ApiResponse.*;

@ApiResponseAnnotation
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentBroker boardCommentBroker;

    @PostMapping("/board-comment")
    public ApiResponse register(@RequestBody BoardCommentRegisterParam param) {
        Map<String, String> map = new HashMap<>();

        map.put("code", boardCommentBroker.save(param));

        return create(map);
    }

    @GetMapping("/board-comments")
    public ApiResponse getBoardComments(@RequestParam("boardId") Long boardId) {
        Map<String, List<BoardCommentResponse>> map = new HashMap<>();

        map.put("contents", boardCommentBroker.findAllByBoardId(boardId));

        return create(map);
    }

    @PatchMapping("/board-comment/{id}")
    public ApiResponse update(@PathVariable("id") Long id, @RequestBody BoardCommentUpdateParam param) {
        Map<String, String> map = new HashMap<>();

        map.put("code", boardCommentBroker.update(id, param));

        return create(map);
    }
}


