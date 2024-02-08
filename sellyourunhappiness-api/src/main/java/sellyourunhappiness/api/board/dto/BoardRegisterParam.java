package sellyourunhappiness.api.board.dto;

public record BoardRegisterParam(
        String type,
        String title,
        String content
) {
}
