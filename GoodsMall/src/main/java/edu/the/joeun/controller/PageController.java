package edu.the.joeun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /**
     * memberAdd.html 을 반환하는 endpoint
     * @return - memberAdd.html 을 반환
     */
    @GetMapping("/member/add")
    public String saveMember(){
        /**
         * 아래와 같이 작성할 경우
         * resources/templates 에서
         * member 폴더 내의 add.html 을 선택한다는 의미
         * // return "member/add";
         * -> return 값으로는 html 파일명을 작성해야 함!
         */
        return "memberAdd";
    }
}
