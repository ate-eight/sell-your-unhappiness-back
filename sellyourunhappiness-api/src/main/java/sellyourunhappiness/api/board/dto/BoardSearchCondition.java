package sellyourunhappiness.api.board.dto;

public record BoardSearchCondition (
    String type,
    String status,
    Integer page
) {

}
