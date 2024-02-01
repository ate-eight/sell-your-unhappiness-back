package sellyourunhappiness.api.board.dto;

import lombok.Builder;
import lombok.Getter;
import sellyourunhappiness.core.board.domain.Board;

import java.time.LocalDateTime;

@Getter
public class BoardSearchResponse {
    private Long id;
    private String type;
    private String status;
    private String title;
    private String content;
    private LocalDateTime createTime;

    @Builder
    public BoardSearchResponse(Long id, String type, String status, String title, String content, LocalDateTime createTime) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    public static BoardSearchResponse toResponse(Board board) {
        return BoardSearchResponse.builder()
                .id(board.getId())
                .type(board.getType().getName())
                .status(board.getStatus().getName())
                .title(board.getTitle())
                .content(board.getContent())
                .createTime(board.getCreateTime())
                .build();
    }
}


