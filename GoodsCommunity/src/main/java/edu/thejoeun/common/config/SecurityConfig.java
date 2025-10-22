package edu.thejoeun.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Configuration : 환경설정용 클래스임을 명시
 * SpringBoot 는 프로젝트를 실행할 때 @Configuration 어노테이션을 가장 먼저 확인함!
 * 객체로 생성해서 내부 코드를 서버 실행 시 모두 실행함.
 * 
 * @Bean
 * 개발자가 수동으로 생성한 객체를
 * SpringBoot 에서 자체적으로 관리하라고 넘기는 어노테이션
 */
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
