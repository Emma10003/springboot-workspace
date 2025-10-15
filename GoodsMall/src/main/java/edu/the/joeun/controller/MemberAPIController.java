package edu.the.joeun.controller;

import edu.the.joeun.model.Member;
import edu.the.joeun.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberAPIController {

    // PostMapping 으로 데이터 저장 시작

    @Autowired
    private MemberService memberService;

    /**
     * const response = await fecth('/api/member/add', {
     *     method: 'POST',
     *     headers: {
     *         'Content-Type': 'application/json'
     *     },
     *     body: JSON.stringify(memberData),
     *      -> json 형태의 데이터를 문자열로 변환해서 Java로 전송!
     * });
     * 
     * 멤버를 등록하므로 PostMapping
     * 
     * 클라이언트가 데이터 저장 요청을 하기 위해 /api/member/add 로 들어오면
     * 
     * js를 통해서 들고 온 문자열(JSON) 형태의 키-값 데이터를
     * Java 모델에 맞는 클래스 변수명칭에
     * 각 키-데이터를 객체 형태로 대입
     * 
     * @param member js에서 가져온 문자열 데이터를
     * @RequestBody 이용하여 Member에 알맞는 객체 형태로 변환해서 값 대입해놓기
     * 
     * 저장된 데이터를 기반으로 service 기능 시작 후
     * service 기능에 대한 결과 유무를 클라이언트에게 전달
     */
    @PostMapping("/api/member/add")
    public void saveMember(@RequestBody Member member){
        memberService.saveMember(member);
    }
}
