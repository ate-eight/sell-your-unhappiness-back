package sellyourunhappiness.core.board_comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sellyourunhappiness.core.board_comment.domain.BoardComment;
import sellyourunhappiness.core.board_comment.infrastructure.BoardCommentRepository;

import java.util.List;

import static sellyourunhappiness.core.board_comment.domain.BoardComment.create;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;

    @Transactional
    public BoardComment save(Long parentId, Long boardId, String content) {
        if (parentId != null) {
            boardCommentRepository.findByParentIdAndBoardId(parentId, boardId)
                    .orElseThrow(() -> new IllegalArgumentException("상위 댓글이 존재하지 않습니다."));
        }

        BoardComment boardComment = create(parentId, boardId, content);
        return boardCommentRepository.save(boardComment);
    }

    @Transactional
    public BoardComment update(Long id, String content) {
        BoardComment boardComment = boardCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id : " + id));
        boardComment.update(content);
        return boardComment;
    }

    public List<BoardComment> findAllByBoardId(Long boardId) {
        return boardCommentRepository.findAllByBoardId(boardId);
    }
}


