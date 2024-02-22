package sellyourunhappiness.api.board_like.controller;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sellyourunhappiness.api.board_like.application.LikeBroker;
import sellyourunhappiness.api.config.response.annotation.ApiResponseAnnotation;
import sellyourunhappiness.api.config.response.aspect.dto.ApiResponse;
import sellyourunhappiness.api.config.security.oauth2.CustomOAuth2User;

@ApiResponseAnnotation
@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeBroker likeBroker;

    @PostMapping("/board/{boardId}/like")
    public ApiResponse createLike(@PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        likeBroker.createLike(customOAuth2User.getEmail(), boardId);
        return ApiResponse.create(new HashMap<>());
        //이용자들 관점에서는 다르다. 좋아요를 눌렀는데 빈값만 보내주면 되면 이용자입장에서는 잘 모른다.
    }

    @PostMapping("/board/{boardId}/dislike")
    public ApiResponse createDislike(@PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        likeBroker.createDislike(customOAuth2User.getEmail(), boardId);
        return ApiResponse.create(new HashMap<>());
    }

}
