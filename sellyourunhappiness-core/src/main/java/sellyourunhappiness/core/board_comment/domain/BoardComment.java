package sellyourunhappiness.core.board_comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id")
    private Long id;

    private Long parentId;

    private Long boardId;

    //private Long userId;

    private String content;

    private LocalDateTime createTime = LocalDateTime.now();

    public BoardComment(Long parentId, Long boardId, String content) {
        this.parentId = parentId;
        this.boardId = boardId;
        this.content = content;
    }

    public static BoardComment create(Long parentId, Long boardId, String content) {
        return new BoardComment(parentId, boardId, content);
    }

    public void update(String content) {
        this.content = content;
    }
}


