package sellyourunhappiness.api.board_comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sellyourunhappiness.api.board_comment.application.BoardCommentBroker;
import sellyourunhappiness.api.board_comment.dto.BoardCommentRegisterParam;
import sellyourunhappiness.api.board_comment.dto.BoardCommentResponse;
import sellyourunhappiness.api.board_comment.dto.BoardCommentUpdateParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentBroker boardCommentBroker;

    @PostMapping("/board-comment")
    public Map<String, String> register(@RequestBody BoardCommentRegisterParam param) {
        Map<String, String> map = new HashMap<>();

        map.put("code", boardCommentBroker.save(param));

        return map;
    }

    @GetMapping("/board-comments/{boardId}")
    public Map<String, List<BoardCommentResponse>> getBoardComments(@PathVariable("boardId") Long boardId) {
        Map<String, List<BoardCommentResponse>> map = new HashMap<>();

        map.put("contents", boardCommentBroker.findAllByBoardId(boardId));

        return map;
    }

    @PatchMapping("/board-comment/{id}")
    public Map<String, String> register(@PathVariable("id") Long id, @RequestBody BoardCommentUpdateParam param) {
        Map<String, String> map = new HashMap<>();

        map.put("code", boardCommentBroker.update(id, param));

        return map;
    }
}


