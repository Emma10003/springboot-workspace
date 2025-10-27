package edu.thejoeun.board.controller;

import edu.thejoeun.board.model.dto.Board;
import edu.thejoeun.board.model.service.BoardService;
import edu.thejoeun.board.model.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j                                  // 로그 기록
@RequestMapping("/api/board")        // 모든(post, get, put, delete..) mapping 앞에 '/api/board'를 공통으로 붙여주겠다.
@RestController                         // 백엔드 데이터 작업 / React 프론트 사용 시 주로 활용 예정
@RequiredArgsConstructor                // @Autowired 대신에 사용
public class BoardController {

    // serviceImpl 에서 재사용된 기능만 활용할 수 있음. (BoardService 는 interface!)
    private final BoardService boardService;

    // 전체 게시물 조회
    @GetMapping("/all")
    public List<Board> getAllBoard() {
        // 전체 게시물 수 조회
        // 페이지네이션 정보 추가
        return boardService.getAllBoard();
    }

    // 게시물 상세 조회
    @GetMapping("/{id}")
    public Board getBoardById(@PathVariable int id) {
        return boardService.getBoardById(id);
    }
}
