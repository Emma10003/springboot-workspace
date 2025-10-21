package edu.thejoeun.member.controller;

import edu.thejoeun.common.util.SessionUtil;
import edu.thejoeun.member.model.dto.Member;
import edu.thejoeun.member.model.service.MemberServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SessionAttributes({"loginUser"})
@Controller
public class MemberController {
    @Autowired
    MemberServiceImpl memberService;

    @GetMapping("/")
    public String pageMain(){
        // return "main";
        return "index";
    }


    @GetMapping("/login")
    public String pageLogin(@CookieValue(value = "saveId", required = false) String savedEmail,
                            Model model){
        if(savedEmail!=null){
            model.addAttribute("savedEmail", savedEmail);
        }
        return "pages/login";
    }

    // GPT or AI -> Model 로 모든 것을 처리함
    // 실무에서는 Model 과 RedirectAttributes 를 구분해서 결과값을 클라이언트에게 전달.
    @PostMapping("/login")
    public String login(@RequestParam String memberEmail,
                        @RequestParam String memberPassword,
                        @RequestParam(required = false) String saveIdCheck,  // 필수로 전달하지 않아도 되는 매개변수
                        HttpSession session,
                        HttpServletResponse res,
                        Model model,
                        RedirectAttributes ra){
        Member member = memberService.login(memberEmail, memberPassword);

        if(member == null) {
            ra.addFlashAttribute("error", "이메일 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/login";  // 일치하지 않는 게 맞다면 로그인 페이지로 돌려보내기
        }

        // 세션에 로그인 정보 저장 -> 이 방법을 쓰게 되면 매변 로그인 정보를 코드마다 세팅해야 함 => 사용 안 함!!
        // session.setAttribute("loginUser", member); // loginUser 이름으로 member 정보가 들어감.
        SessionUtil.setLoginUser(session, member);

        // 쿠키에 사용자 정보 저장 (보안 상 민감하지 않은 부분만 저장)
        Cookie userIdCookie = new Cookie("saveId", memberEmail);
        userIdCookie.setPath("/");
        // 유저 아이디를 아이디 저장이 체크되어 있으면(saveId.equals("on")) 30일 간 유저 아이디 저장
        // 체크되어 있지 않으면 유저 아이디를 쿠키에 저장하지 않겠다.
        /*
         체크박스에서 value 가 없을 때
         - 체크가 된 경우    : on
         - 체크가 안 된 경우 : null
         아이디 저장과 같이 단순 체크는 on - null 이용해서 체크 유무 확인
         아이디를 작성 안 했는데 쿠키에 저장할 이유가 없으므로,
         아이디값을 작성하고 아이디 저장 체크를 했을 경우에만 30일 동안 아이디 명칭을 저장하겠다.
         */
        if("on".equals(saveIdCheck)){
        // if(userIdCookie != null && saveId.equals("on")) {
            //                    60초 * 60분 * 24시간 * 30일 => 총 30일 동안 유효하게 설정
            userIdCookie.setMaxAge(60 * 60 * 24 * 30); // 30일 초 단위로 지정
        } else {
            userIdCookie.setMaxAge(0); // 클라이언트 쿠키 삭제
        }
        res.addCookie(userIdCookie);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse res) {

        SessionUtil.invalidateLoginUser(session); // 세션 삭제

        Cookie userIdCookie = new Cookie("saveId", null); // 쿠키 삭제
        userIdCookie.setMaxAge(0);
        userIdCookie.setPath("/");
        res.addCookie(userIdCookie);
        return "redirect:/";  // 로그아웃 선택 시 모든 쿠키 데이터 지우고 메인으로 돌려보내기
    }
}
