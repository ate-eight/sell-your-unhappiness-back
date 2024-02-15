package sellyourunhappiness.api.board_like.controller;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sellyourunhappiness.api.board_like.application.BoardLikeBroker;
import sellyourunhappiness.api.config.response.annotation.ApiResponseAnnotation;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;

@ApiResponseAnnotation
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeBroker boardLikeBroker;

    @PostMapping("/board/{boardId}/{type}")
    public ApiResponse like(@PathVariable("boardId") Long boardId, @PathVariable("type") String type,
                                  @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        boardLikeBroker.like(customOAuth2User.getEmail(), boardId, type);
        return ApiResponse.create(new HashMap<>());
    }
}


