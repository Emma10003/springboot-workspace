package edu.thejoeun.member.controller;

import edu.thejoeun.common.exception.ForbiddenException;
import edu.thejoeun.common.exception.UnauthorizedException;
import edu.thejoeun.common.util.FileUploadService;
import edu.thejoeun.common.util.SessionUtil;
import edu.thejoeun.member.model.dto.Member;
import edu.thejoeun.member.model.service.MemberServiceImpl;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberAPIController {
     private final MemberServiceImpl memberService;
     private final FileUploadService fileUploadService;

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody Map<String, String> loginData, HttpSession session){
        String memberEmail = loginData.get("memberEmail");
        String memberPassword = loginData.get("memberPassword");
        Map<String, Object> res = memberService.loginProcess(memberEmail, memberPassword,session);
        return res;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session){
        return  memberService.logoutProcess(session);
    }

    @GetMapping("/check")
    public Map<String, Object> checkLoginStatus(HttpSession session){
        return memberService.checkLoginStatus(session);
    }

    // const res = axios.post("/api/auth/signup",signupData);
    // PostMapping 만들기
    // mapper.xml -> mapper.java -> service.java -> serviceImpl.java apiController.java
    // 완성

    @PostMapping("/signup")
    public void saveSignup(@RequestPart Member member,
                           @RequestPart(required = false) MultipartFile profileImage){
      log.info("===회원가입 요청===");
      log.info("요청 데이터 - 이름 : {}, 이메일 : {}",member.getMemberName(),member.getMemberEmail());

      try {
          memberService.saveMember(member, profileImage);
          log.info("회원가입 성공 - 이메일 : {}", member.getMemberEmail());
          /**
           * 브로드캐스트를 통해서
           * 모든 사람들에게 ㅇㅇㅇ 님이 가입했습니다. 알림 설정
           */
      } catch (Exception e){
          log.error("회원가입 실패 - 이메일 : {}, 에러 : {}",member.getMemberEmail(), e.getMessage());
      }

    }

    /**
     * 상품 이미지 업로드를 프로필 사진 업로드처럼 product-images 폴더에 업데이트 되도록 설정.
     * -> 이미지 업로드한 데이터를 가져오고 가져가는 서버 - 웹 페이지 작업 같이 들어감!
     * 메서드 명칭: updateProduct...
     *
     * fetchMypageEditWithProfile(axios, formData, profileFile, navigate, setIsSubmitting);
     * 이 데이터를 받기 위한 매개변수 수정이 일어날 것.
     * @param updateData
     * @param session
     * @return
     */
    @PutMapping("/update")
    public Map<String, Object> updateMypage(@RequestBody Map<String, Object> updateData, HttpSession session) {
        log.info("📍 회원정보 수정 요청");
        try {
            Member m = new Member();
            m.setMemberPhone(updateData.get("memberPhone").toString());
            m.setMemberEmail(updateData.get("memberEmail").toString());
            m.setMemberName(updateData.get("memberName").toString());
            m.setMemberAddress(updateData.get("memberAddress").toString());

            // 새 비밀번호가 있는 경우
            String newPassword = (String) updateData.get("memberPassword");
            if(newPassword != null && !newPassword.isEmpty()){
                m.setMemberPassword(newPassword);
            }

            // 현재 비밀번호
            String currentPassword = (String) updateData.get("currentPassword");
            Map<String, Object> res = memberService.updateMember(m, currentPassword, session);
            // 서비스에서 성공/실패에 대한 결과를 res 에 담고 프론트엔드에 전달
            log.info("✉️ 회원정보 수정 결과: {}", res.get("message"));
            return res;

        } catch (Exception e) {
            log.error("❌ 서비스에 접근했거나, 서비스 가기 전에 문제가 발생해 회원정보 수정 실패 - 에러: {}", e.getMessage());
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", "회원정보 수정 중 오류가 발생했습니다.");
            return res;
        }
    }

    // 컨트롤러가 할 일
    // : 요청을 받아서 Service 로 넘기고, 결과를 응답하는 역할만 함!
    @PostMapping("/profile-image")
    public ResponseEntity<Map<String, Object>> uploadProfileImage(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam("memberEmail") String memberEmail,
                                                                  HttpSession session){
        Map<String, Object> res = new HashMap<>();

        try {
            Member loginUser = SessionUtil.getLoginUser(session);
            String imageUrl = memberService.updateProfileImage(loginUser, memberEmail, file, session);

            res.put("success", true);
            res.put("message", "프로필 이미지가 업데이트 되었습니다.");
            res.put("imageUrl", imageUrl);
            log.info("✅ 프로필 이미지 업로드 성공 - 이메일: {}, 파일명: {}", memberEmail, file.getOriginalFilename());
            return ResponseEntity.ok(res);  // 업데이트가 무사히 되면 200만 전달

        // 개발자가 만든 exception 은 최상위에 작성
        // Java 에서 기본으로 제공하는 exception 은 최상위가 아닌 순서부터 작성
        // exception들의 부모인 Exception 은 맨 마지막에 작성.
        // 부모 Exception 까지 올 때는 어떤 예외상황이 발생한 건지 파악하지 못 한 상태!!
        } catch (UnauthorizedException e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.status(401).body(res);

        } catch (ForbiddenException e) {
            // 본인 확인
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.status(403).body(res);

        } catch (IllegalArgumentException e) {
            // 파일 유효성 검증
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(res);

        } catch (Exception e) {
            log.error("❌ 프로필 이미지 업로드 실패 - 이메일: {}, 오류: {}", memberEmail, e.getMessage());
            res.put("success", false);
            res.put("message", "서버 오류가 발생했습니다 : " + e.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }
}


