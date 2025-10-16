package edu.the.joeun.controller;

import edu.the.joeun.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RestController
@Controller
public class LoginAPIController {

    @Autowired
    private UsersService usersService;

    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String login(HttpSession session,
                        @RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        return usersService.processLogin(email, password, session, model);
    }
}
