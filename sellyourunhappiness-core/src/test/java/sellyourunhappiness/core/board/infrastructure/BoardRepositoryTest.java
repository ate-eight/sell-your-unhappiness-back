package sellyourunhappiness.core.board.infrastructure;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board.domain.enums.BoardStatus;
import sellyourunhappiness.core.board.domain.enums.BoardType;
import sellyourunhappiness.core.config.page.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Nested
@SpringBootTest
@DisplayName("게시판 인프라 계층")
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @AfterEach
    void clear() {
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 저장")
    void save() {
        //given
        Board request = Board.create("회사", "회사 고민글", "고민 내용");

        //when
        Board board = boardRepository.save(request);

        //then
        assertThat(board.getType()).isEqualTo(request.getType());
        assertThat(board.getStatus()).isEqualTo(request.getStatus());
        assertThat(board.getTitle()).isEqualTo(request.getTitle());
        assertThat(board.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void findById() {
        //given
        Board request = boardRepository.save(Board.create("회사", "회사 고민글", "고민 내용"));

        //when
        Board board = boardRepository.findById(request.getId()).get();

        //then
        assertThat(board.getType()).isEqualTo(request.getType());
        assertThat(board.getStatus()).isEqualTo(request.getStatus());
        assertThat(board.getTitle()).isEqualTo(request.getTitle());
        assertThat(board.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("게시글 페이징 조회")
    void search() {
        //given
        int total = 97;
        int page = 2;
        for (int i = 0; i < total; i++) {
            boardRepository.save(Board.create("회사", "회사 고민글" + i, "고민 내용" + i));
        }

        //when
        Page<Board> search = boardRepository.findByTypeAndStatusAllDesc(BoardType.COMPANY, BoardStatus.SALE, new PageRequest(page).of());

        //then
        assertThat(search.getNumber()).isEqualTo(page - 1);
        assertThat(search.getTotalElements()).isEqualTo(97);
        assertThat(search.getTotalPages()).isEqualTo(total / search.getSize() + 1);
    }
}


