package edu.thejoeun.common.aop;


import java.util.Arrays;

import edu.thejoeun.member.model.dto.Member;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Before("PointcutBundle.controllerPointCut()")
    public void beforeController(JoinPoint jp) {

        String className = jp.getTarget().getClass().getSimpleName();

        String methodName = jp.getSignature().getName() + "()";

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String ip = getRemoteAddr(req);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("[%s.%s] 요청 / ip : %s", className, methodName, ip));


        if(req.getSession().getAttribute("loginMember") != null) {

            String memberEmail =
                    ( (Member)req.getSession().getAttribute("loginMember") ).getMemberEmail();

            sb.append(String.format(", 요청 회원 : %s", memberEmail));
        }

        log.info(sb.toString());
    }

    @Around("PointcutBundle.serviceImplPointCut()")
    public Object aroundServiceImpl(ProceedingJoinPoint pjp) throws Throwable {

        String className = pjp.getTarget().getClass().getSimpleName();


        String methodName = pjp.getSignature().getName() + "()";

        log.info("========== {}.{} 서비스 호출 ==========", className, methodName);


        log.info("Parameter : {}", Arrays.toString(pjp.getArgs()));


        long startMs = System.currentTimeMillis();

        Object obj = pjp.proceed();

        long endMs = System.currentTimeMillis();

        log.info("Running Time : {}ms", endMs - startMs);


        log.info("===================================================================================");


        return obj;
    }



    @AfterThrowing(pointcut = "@annotation(org.springframework.transaction.annotation.Transactional)",
            throwing = "ex")
    public void transactionRollback(JoinPoint jp, Throwable ex) {
        log.info("***** 트랜잭션이 롤백됨 {} *****", jp.getSignature().getName());
        log.error("[롤백 원인] : {}", ex.getMessage());
    }


    private String getRemoteAddr(HttpServletRequest request) {

        String ip = null;

        ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
/*
advice : 끼워넣을 코드 (메서드 = 기능)
pointCut : 실제 advice 를 적용할 joinPoint(지점)

PointCut 작성방법
    execution ( [접근제한자(생략가능)] 리턴타입 클래스명 메서드명 ([파라미터]) )

    클래스명은 패키지명부터 모두 작성

    * : 모든
    // 메서드명(..) 에서 매개변수 .. : 매개변수가 0~n개 (개수 상관 없음)
 */
