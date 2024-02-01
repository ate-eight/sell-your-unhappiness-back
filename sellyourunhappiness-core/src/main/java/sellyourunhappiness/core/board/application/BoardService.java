package sellyourunhappiness.core.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sellyourunhappiness.core.board.domain.Board;
import sellyourunhappiness.core.board.domain.enums.BoardStatus;
import sellyourunhappiness.core.board.domain.enums.BoardType;
import sellyourunhappiness.core.board.infrastructure.BoardRepository;
import sellyourunhappiness.core.config.converter.EnumConverterUtils;
import sellyourunhappiness.core.config.page.PageModel;
import sellyourunhappiness.core.config.page.PageRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board save(String type, String title, String content) {
        Board board = Board.create(type, title, content);
        return boardRepository.save(board);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id : " + id));
    }

    public PageModel<Board> findByTypeAndStatusAllDesc(String type, String status, Integer page) {
        return new PageModel<>(boardRepository.findByTypeAndStatusAllDesc(
                EnumConverterUtils.ofName(BoardType.class, type),
                EnumConverterUtils.ofName(BoardStatus.class, status),
                new PageRequest(page).of()));
    }
}


