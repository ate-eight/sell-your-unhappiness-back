package sellyourunhappiness.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sellyourunhappiness.api.board.application.BoardBroker;
import sellyourunhappiness.api.board.dto.BoardRegisterParam;
import sellyourunhappiness.api.board.dto.BoardResponse;
import sellyourunhappiness.api.board.dto.BoardSearchCondition;
import sellyourunhappiness.api.board.dto.BoardSearchResponse;
import sellyourunhappiness.api.config.page.PageResponse;
import sellyourunhappiness.api.config.enums.EnumBean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardBroker boardBroker;
    private final EnumBean enumBean;

    @PostMapping("/board")
    public Map<String, String> register(@RequestBody BoardRegisterParam param) {
        Map<String, String> map = new HashMap<>();

        map.put("code", boardBroker.save(param));

        return map;
    }

    @GetMapping("/board/{id}")
    public BoardResponse getBoardById(@PathVariable("id") Long id) {
        return boardBroker.findById(id);
    }

    @GetMapping("/boards")
    public PageResponse<BoardSearchResponse> searchBoards(BoardSearchCondition condition){
        return boardBroker.findByTypeAndStatusAllDesc(condition);
    }

    @GetMapping("/board/types")
    public Map<String, List<String>> getBoardTypes() {
        Map<String, List<String>> map = new LinkedHashMap<>();

        map.put("types", enumBean.get("BoardType"));

        return map;
    }
}


