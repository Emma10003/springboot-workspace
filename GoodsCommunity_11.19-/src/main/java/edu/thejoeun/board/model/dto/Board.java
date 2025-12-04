package edu.thejoeun.board.model.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    private int id;
    private String title;
    private String content;
    private String writer;
    private int viewCount;
    private String createdAt;
    private String updatedAt;
    private Integer ranking;
    private String popularUpdateAt;
    private String boardMainImage;
    // DB에 저장할 때는 리스트 -> 문자열 형태로 저장, 콤마(,)로 구분지어서 하나의 문자열로 저장할 예정
    private String boardDetailImage;
    /**
     * 1. Oracle DB 가서 alter 이용해서 board_image컬럼 varchar2 로 추가 ✅
     * 2. config.properties 가서 board-upload-image 경로 생성 ✅
     * 3. WebConfig 설정 ✅
     * 4. fileUploadService 에서 게시물 이미지 올렸을 때 게시물 번호로 폴더 생성 후 내부에 이미지 파일 생성하기.
     * 5. 게시물 작성 시 폴더에 이미지 저장되고, DB에 경로 + 파일명 추가되는지 확인
     */
}










