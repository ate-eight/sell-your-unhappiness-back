package sellyourunhappiness.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sellyourunhappiness.api.board.application.BoardBroker;
import sellyourunhappiness.api.board.dto.BoardRegisterParam;
import sellyourunhappiness.api.board.dto.BoardResponse;
import sellyourunhappiness.api.board.dto.BoardSearchCondition;
import sellyourunhappiness.api.board.dto.BoardSearchResponse;
import sellyourunhappiness.api.config.page.PageResponse;
import sellyourunhappiness.api.config.enums.EnumBean;
import sellyourunhappiness.api.config.response.annotation.ApiResponseAnnotation;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static sellyourunhappiness.api.config.response.aspect.dto.ApiResponse.*;

@ApiResponseAnnotation
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardBroker boardBroker;
    private final EnumBean enumBean;

    @PostMapping("/board")
    public ApiResponse register(@RequestBody BoardRegisterParam param) {
        Map<String, String> map = new HashMap<>();

        map.put("message", boardBroker.save(param));

        return create(map);
    }

    @GetMapping("/board/{id}")
    public ApiResponse getBoardById(@PathVariable("id") Long id) {
        return create(boardBroker.findById(id));
    }

    @GetMapping("/boards")
    public ApiResponse searchBoards(BoardSearchCondition condition){
        return create(boardBroker.findByTypeAndStatusAllDesc(condition));
    }

    @GetMapping("/board/types")
    public ApiResponse getBoardTypes() {
        Map<String, List<String>> map = new LinkedHashMap<>();

        map.put("types", enumBean.get("BoardType"));

        return create(map);
    }
}


