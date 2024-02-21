package sellyourunhappiness.api.board_like.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sellyourunhappiness.api.board_like.application.BoardLikeBroker;
import sellyourunhappiness.api.board_like.dto.BoardLikeParam;
import sellyourunhappiness.api.config.enums.EnumBean;
import sellyourunhappiness.api.config.response.annotation.ApiResponseAnnotation;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;

@ApiResponseAnnotation
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeBroker boardLikeBroker;
    private final EnumBean enumBean;
    @PostMapping("/board/{boardId}/{type}")
    public ApiResponse like(@PathVariable("boardId") Long boardId, @PathVariable("type") String type,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Map<String, String> map = new HashMap<>();

        map.put("message", boardLikeBroker.like(customOAuth2User.getEmail(), boardId, type));

        return ApiResponse.create(map);
    }

    @PostMapping("/test")
    public void test(@RequestBody BoardLikeParam boardLikeParam) {
        System.out.println();
    }


    @GetMapping("/like/types")
    public ApiResponse getLikeTypes() {
        Map<String, List<String>> map = new LinkedHashMap<>();

        map.put("types", enumBean.get("LikeType"));

        return ApiResponse.create(map);
    }
}


