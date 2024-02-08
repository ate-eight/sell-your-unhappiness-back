package sellyourunhappiness.api.board.dto;

import lombok.Builder;
import lombok.Getter;
import sellyourunhappiness.core.board.domain.Board;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    private String type;
    private String status;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;

    @Builder
    public BoardResponse(String type, String status, String title, String content, LocalDateTime createTime, LocalDateTime modifiedTime) {
        this.type = type;
        this.status = status;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
    }

    public static BoardResponse toResponse(Board board) {
        return BoardResponse.builder()
                .type(board.getType().getName())
                .status(board.getStatus().getName())
                .title(board.getTitle())
                .content(board.getContent())
                .createTime(board.getCreateTime())
                .modifiedTime(board.getModifiedTime())
                .build();
    }
}


