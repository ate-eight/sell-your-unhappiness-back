package sellyourunhappiness.core.board_like.domain;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board_like.domain.converter.LikeTypeConverter;
import sellyourunhappiness.core.board_like.domain.enums.LikeType;
import sellyourunhappiness.core.user.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long boardId;

    @Convert(converter = LikeTypeConverter.class)
    private LikeType likeType;
    @Builder
    private BoardLike(Long userId, Long boardId, LikeType likeType) {
        this.userId = userId;
        this.boardId = boardId;
        this.likeType = likeType;
    }

<<<<<<< HEAD
    public static BoardLike create(Long boardId, Long userId, LikeType likeType) {
        return BoardLike.builder()
                .userId(userId)
                .boardId(boardId)
=======
    public static BoardLike create(User user, Board board, LikeType likeType) {
        return BoardLike.builder()
                .userId(user.getId())
                .boardId(board.getId())
>>>>>>> 354a831 (feat : 좋아요 기능 구현)
                .likeType(likeType)
                .build();
    }

}
