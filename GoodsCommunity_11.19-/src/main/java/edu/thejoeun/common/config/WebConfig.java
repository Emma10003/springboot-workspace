package edu.thejoeun.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
BackEnd 와 FrontEnd 가 나누어 작업할 때 사용
corsConfigurer 설정은
WebConfig 이름을 사용하거나 CorsConfig 라는 명칭을 사용하기도 함
회사에서 지정한 명명규칙을 따를 것
*/

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // profile.upload.path 와 같이 상세하게 변수명을 지정하는 게 좋다.
    @Value("${file.upload.path}")
    private String fileUploadPath;

    @Value("${file.product.upload.path}")
    private String productUploadPath;

    @Value("${file.board.upload.path}")
    private String boardUploadPath;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // REST API CORS 설정
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:57472", "http://localhost:3000")
                        .allowCredentials(true)
                        .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
                        .allowedHeaders("*");
                
                // WebSocket CORS 설정 추가
                registry.addMapping("/ws/**")
                        .allowedOrigins("http://localhost:57472", "http://localhost:3000")
                        .allowCredentials(true)
                        .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    // 프로필 이미지 정적 리소스 매핑 추가
    // ctrl + o -> Override
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 프로필 이미지 폴더 수정 -> DB에서 /profile_images/** 를 가져오면 fileUploadPath/.. 에 저장한다는 약속
        registry.addResourceHandler("/profile_images/**")
                .addResourceLocations("file:" + fileUploadPath + "/"); // 맨 끝에 "/" 안 붙이면 폴더명칭 뒤에 바로 이미지 명칭 붙어서 에러 발생

        // 상품 이미지 : product_images/** 라는 변수명으로 데이터를 가져와서 활용하겠다는 요청이 들어오면
        // 실제 파일 시스템 경로에서 이미지를 가져와 사용.
        // 현재는 DB 명칭과 바탕화면 폴더 명칭을 동일하게 했지만, DB 명칭은 img_prd 같이 작성하고,
        // 바탕화면 폴더는 풀네임으로 작성하여, 외부에서 함부로 데이터를 가져가거나 서버 정보를 노출시키지 않도록
        // 보호할 수 있다.
        /*
          동작 방식
          클라이언트가 /product_images/제품번호/main.jpg 요청
          -> 실제 파일 : 바탕화면/product_images/제품번호/main.jpg 반환

          주의사항 :
            "file:" 뒤에 공백 없이 바로 경로 연결!! 띄어쓰기 없어야 함!!
            경로 끝에 "/" 반드시 붙이기!! (안 붙이면 경로 인식 오류)
            프로필/상품 이미지 경로가 각각 독립적으로 설정되었는지 반드시 확인할 것!!
         */
        registry.addResourceHandler("/product_images/**")
                .addResourceLocations("file:" + productUploadPath + "/");

        // 게시물 이미지 폴더 수정
        registry.addResourceHandler("/board_images/**")
                .addResourceLocations("file:" + boardUploadPath + "/");


    }
}

