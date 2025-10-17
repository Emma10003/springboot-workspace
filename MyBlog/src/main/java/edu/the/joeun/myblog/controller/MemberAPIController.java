package edu.the.joeun.myblog.controller;

import edu.the.joeun.myblog.model.Member;
import edu.the.joeun.myblog.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 백엔드가 프론트엔드로 데이터 제대로 보내는지 확인용 컨트롤러
// 프론트엔드 요청사항을 전달하는 컨트롤러
@RestController
public class MemberAPIController {
    @Autowired
    private MemberServiceImpl memberService;

    // 백엔드에서 데이터가 바로 html 파일로 전송
    @GetMapping("/api/memberList")
    public List<Member> getMemberList(){
        return memberService.selectMemberList();
    }

    /**
     * Model model = login 에서만 사용, 다른 데에서는 쓰지 않음 ^v^
     * myPage 로
     * member 명칭을 활용해서 백엔드 확인 없이
     * 프론트엔드로 바로 데이터 전송하는 방법
     * 경력 3년 이하에게는 추천하지 않으며, 고전 방식.
     * 현업에서 많이 사용하지 않음!
        @GetMapping("/api/member/{id}")
        public String getMemberById(@PathVariable int id, Model model){
            Member member = memberService.getMemberById(id);
            model.addAttribute("member",member);
            return "myPage";
    }
    */

    /**
     * 백엔드 전용 로직.
     * 백엔드에서 데이터를 제대로 조회하고 있는지 확인하는 로직
     * 리액트 - 뷰 - 앵귤러 - js 소통에서도 모두 이 방식을 활용!
     *
     * http://localhost:8080/api/member/1
     *
     * 관리자가 모든 유저 리스트에서
     * 유저 한 명을 선택하여 상세보기로 이동할 때 사용
     * @param id    @PathVariable 주소창에 값으로 넣을 변수명칭이나 데이터
     * @return      select * from 테이블 where id = ${id}
     *              -> id 값으로 DB에서 조회된 데이터를 백엔드로 전달.
     */
    @GetMapping("/api/member/{id}")
    public Member getMemberById(@PathVariable int id){
        return memberService.getMemberById(id);
    }

    /**
     * 마이페이지에서 본인의 정보를 조회할 때 사용
     *
     * 추가적으로 session 정보와
     * session 정보를 전달할 Model model 을 활용하여 전송할 예정!
     * @param id
     * @return
     */
    @GetMapping("/api/mypage")
    public Member getMemberByMyPage(int id){
        return memberService.getMemberById(id);
    }


    @PostMapping("/api/member/register")
    public void saveMember(@ModelAttribute Member member) {
        memberService.saveMember(member);
    }
}
