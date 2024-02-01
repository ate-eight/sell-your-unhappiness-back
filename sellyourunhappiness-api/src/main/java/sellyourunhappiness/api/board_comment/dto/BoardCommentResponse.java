package sellyourunhappiness.api.board_comment.dto;

import lombok.Builder;
import lombok.Getter;
import sellyourunhappiness.core.board_comment.domain.BoardComment;

import java.time.LocalDateTime;

@Getter
public class BoardCommentResponse {
    private Long id;
    private Long parentId;
    private String content;
    private LocalDateTime createTime;

    @Builder
    public BoardCommentResponse(Long id, Long parentId, String content, LocalDateTime createTime) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
        this.createTime = createTime;
    }

    public static BoardCommentResponse toResponse(BoardComment boardComment) {
        return BoardCommentResponse.builder()
                .id(boardComment.getId())
                .parentId(boardComment.getParentId())
                .content(boardComment.getContent())
                .createTime(boardComment.getCreateTime())
                .build();
    }
}


