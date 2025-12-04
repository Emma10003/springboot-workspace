package edu.thejoeun.board.model.service;


import edu.thejoeun.board.model.dto.Board;
import edu.thejoeun.board.model.mapper.BoardMapper;
import edu.thejoeun.common.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    //@Autowired
    // Autowired 보다 RequiredArgsConstructor 처리해주는 것이
    // 상수화하여 Mapper 를 사용할 수 있으므로 안전 -> 내부 메서드나 데이터 변경 불가
    private final BoardMapper boardMapper;
    private final FileUploadService fileUploadService;

    @Override
    public List<Board> getAllBoard() {
        return boardMapper.getAllBoard();
    }

    @Override
    public Board getBoardById(int id) {
        // 게시물 상세조회를 선택했을 때 해당 게시물의 조회수 증가
        boardMapper.updateViewCount(id);

        Board b = boardMapper.getBoardById(id);
        // 게시물 상세조회를 위해 id를 입력하고, 입력한 id 에 해당하는 게시물이
        // 존재할 경우에는 조회된 데이터 전달
        // 존재하지 않을 경우에는 null 전달
        return b != null ? b : null;
    }


    /*
    TODO: 게시물 메인 이미지, 게시물 상세 이미지 전달받는 매개변수 두 가지 추가
     */
    @Override
    public void createBoard(Board board, MultipartFile file) {
        log.info("💡 게시글 등록 시작 - ID: {}", board.getId());

        try {
            // 이미지 없이 일단 게시글 등록
            boardMapper.insertBoard(board);

            if(file != null || !file.isEmpty()){  // 이미지가 존재하는 경우
                log.info("💡 이미지 저장 시작");
                // 폴더 및 파일 생성 작업
                String imageUrl = fileUploadService.uploadBoardImage(file, board.getId(), "main");
                // 이미지 URL을 board 에 설정하고
                board.setBoardMainImage(imageUrl);
                // DB 업데이트
                boardMapper.updateBoard(board);
                log.info("✅ 게시글 등록 성공 - ID: {}, imageUrl: {}", board.getId(), imageUrl);
            } else {
                log.info("💡 저장할 이미지가 존재하지 않습니다.");
            }

        } catch (Exception e) {
            log.error("❌ 게시글 등록 중 오류가 발생했습니다. {}", e.getMessage());
            throw new RuntimeException("게시글 등록에 실패했습니다.");
        }

    }
}
