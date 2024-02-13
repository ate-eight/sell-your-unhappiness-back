package sellyourunhappiness.core.board.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sellyourunhappiness.core.board.infrastructure.BoardRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("게시판 서비스 계층")
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시글 없는 게시물 조회시 예외 처리")
    void findByIdException() {
        //given
        when(boardRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> boardService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다. id : " + 1L);
    }
}


