package edu.thejoeun.member.controller;


import edu.thejoeun.member.model.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SessionAttributes("{authKey}")
@RestController
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;


    @PostMapping("signup") // api : /email/signup
    public int signup(@RequestBody String email){
        String authKey = emailService.sendMail("signup", email);
        if(authKey != null){
            return 1;
        }
        return 0;
    }

    @PostMapping("checkAuthKey")
    public int checkAuthKey(@RequestBody Map<String, Object> map){
        return emailService.checkAuthKey(map);
    }
}



