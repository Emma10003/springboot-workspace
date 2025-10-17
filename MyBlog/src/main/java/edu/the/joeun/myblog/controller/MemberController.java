package edu.the.joeun.myblog.controller;

// @RestController 는 ReactJS 할 때 까지 안녕~~
// or 백엔드 로직 작업 우선 진행할 때 필요할 수 있다!

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
    @GetMapping("/")
    public String getIndexPage(){
        return "index";
    }

    @GetMapping("/member/list")
    public String getMemberPage(){
        return "member";
    }

    @GetMapping("/member/register")
    public String getMemberRegisterPage(){
        return "member_register";
    }
}
