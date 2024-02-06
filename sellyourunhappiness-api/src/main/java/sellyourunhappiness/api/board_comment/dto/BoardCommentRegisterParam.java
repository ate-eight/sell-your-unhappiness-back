package sellyourunhappiness.api.board_comment.dto;

public record BoardCommentRegisterParam(
        Long parentId,
        Long boardId,
        String content
) {
}
