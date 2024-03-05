package sellyourunhappiness.core.board_comment.infrastructure;

import static org.assertj.core.api.Assertions.*;
import static sellyourunhappiness.core.board_comment.domain.BoardComment.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sellyourunhappiness.core.board_comment.domain.BoardComment;

@Nested
@SpringBootTest
@DisplayName("게시판 댓글 인프라 계층")
class BoardCommentRepositoryTest {

    @Autowired
    BoardCommentRepository boardCommentRepository;

    @AfterEach
    void clear() {
        boardCommentRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 댓글 저장")
    void save() {
        //given
        BoardComment request = create(0L, 1L, "테스트 댓글");

        //when
        BoardComment boardComment = boardCommentRepository.save(request);

        //then
        assertThat(boardComment.getParentId()).isEqualTo(request.getParentId());
        assertThat(boardComment.getBoardId()).isEqualTo(request.getBoardId());
        assertThat(boardComment.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("상위 댓글 null")
    void saveParentIdNull() {
        //given
        BoardComment request = create(null, 1L, "테스트 댓글");

        //when
        BoardComment boardComment = boardCommentRepository.save(request);

        //then
        assertThat(boardComment.getParentId()).isEqualTo(0L);
        assertThat(boardComment.getBoardId()).isEqualTo(request.getBoardId());
        assertThat(boardComment.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("전체 댓글 출력")
    void findAllByBoardId() {
        //given
        BoardComment request = create(null, 1L, "테스트 댓글1");

        boardCommentRepository.save(request);

        //when
        List<BoardComment> boardComments = boardCommentRepository.findAllByBoardId(1L);

        //then
        assertThat(boardComments.get(0).getParentId()).isEqualTo(0L);
        assertThat(boardComments.get(0).getBoardId()).isEqualTo(request.getBoardId());
        assertThat(boardComments.get(0).getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("부모 댓글 Id 게시판 댓글 Id로 단건 조회")
    void findByParentIdAndBoardId() {
        //given
        BoardComment request = create(null, 1L, "테스트 댓글1");

        boardCommentRepository.save(request);

        //when
        BoardComment boardComment = boardCommentRepository.findByIdAndBoardId(0L, 1L).get();

        //then
        assertThat(boardComment.getParentId()).isEqualTo(0L);
        assertThat(boardComment.getBoardId()).isEqualTo(request.getBoardId());
        assertThat(boardComment.getContent()).isEqualTo(request.getContent());
    }
}


