package sellyourunhappiness.core.board_comment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sellyourunhappiness.core.board_comment.infrastructure.BoardCommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("게시판 댓글 서비스 계층")
class BoardCommentServiceTest {

    @InjectMocks
    BoardCommentService boardCommentService;

    @Mock
    BoardCommentRepository boardCommentRepository;

    @Test
    @DisplayName("댓글 등록시 null이 아닐때 상위 댓글이 없는 경우")
    void findByIdException() {
        //given
        when(boardCommentRepository.findByParentIdAndBoardId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> boardCommentService.save(1L, 1L, "테스트내용"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상위 댓글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("댓글 수정시 해당 댓글이 존재하지 않는 경")
    void updateException() {
        //given
        when(boardCommentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> boardCommentService.update(1L, "테스트내용"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 댓글이 존재하지 않습니다. id : " + 1L);
    }
}