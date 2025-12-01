package edu.thejoeun.common.util;

// 파일 이미지를 업로드 할 때, 변수이름을 상세히 작성하는 것이 좋다!
// 프로필 이미지, 게시물 이미지, 상품 이미지 등등... 여러 이미지가 있을 수 있기 때문!

// import lombok.Value;  ->  DB 관련 Value (DB 컬럼값)
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;  // 스프링부트 properties 에서 사용한 데이터 가져오기
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadService {

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 프로필 이미지 업로드
     * @param file 업로드할 이미지 파일
     * @return 저장된 파일의 경로 (DB에 저장할 상대 경로
     * @throws IOException 파일 처리 중 오류 발생 시 예외 처리
     */
    public String uploadProfileImage(MultipartFile file) throws IOException {
        // 파일이 비어있는지 확인
        if(file.isEmpty()){
            throw new IOException("업로드할 파일이 없습니다.");
        }

        // 업로드 디렉토리 생성 (폴더가 존재하지 않는 경우, 디렉토리 = 폴더. 컴퓨터 만든 회사에서 지칭하는 이름이 다름)
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            boolean created = uploadDir.mkdirs();
            if(!created){
                throw new IOException("업로드 디렉토리 생성에 실패했습니다." + uploadPath);
            }
            log.info("✅ 업로드 디렉토리 생성: {}", uploadPath);
        }

        // 원본 파일명과 확장자 추출 (originalFileName)
        String 클라이언트가업로드한파일이름 = file.getOriginalFilename();
        // 파일 이름이 없는 경우
        if(클라이언트가업로드한파일이름 == null || 클라이언트가업로드한파일이름.isEmpty()){
            throw new IOException("파일 이름이 유효하지 않습니다.");
        }

        // 확장자 (extension), 마지막마침표의위치 (lastDotIndex)
        String 확장자 = "";
        int 마지막마침표의위치 = 클라이언트가업로드한파일이름 .lastIndexOf(".");  // p.i.g.png 같이 쓸 수도 있으니까
        if(마지막마침표의위치 > 0){
            확장자 = 클라이언트가업로드한파일이름.substring(마지막마침표의위치); // 마지막마침표부터 끝까지 확장자라는 변수에 저장 (.png, .jpg, ...)
        }

        // 고유한 파일명 생성 (UUID 사용) - uniqueFileName
        String 하나_밖에_없는_파일이름 = UUID.randomUUID().toString() + 확장자;

        // 파일저장될경로 (filePath)
        // uploadPath : 프로필 사진이 위치할 폴더
        // 하나_밖에_없는_파일이름 : 폴더 내에서 겹칠 일이 없는 파일명칭.
        Path 파일저장될경로 = Paths.get(uploadPath, 하나_밖에_없는_파일이름);

        // 파일 저장
        try {
            Files.copy(file.getInputStream(), 파일저장될경로, StandardCopyOption.REPLACE_EXISTING);
            log.info("프로필 이미지 업로드 성공: {} -> {}", 클라이언트가업로드한파일이름, 하나_밖에_없는_파일이름);
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: {}", e.getMessage());
            throw new IOException("파일 저장에 실패했습니다 : " + e.getMessage());
        }

        // DB에서 저장할 상대경로 반환 (웹에서 접근 가능한 경로)
        // 폴더 경로는 WebConfig 의 경로명(.addResourceHandler)과 일치시킬 것!
        return "/profile_images/" + 하나_밖에_없는_파일이름;
    }
}
