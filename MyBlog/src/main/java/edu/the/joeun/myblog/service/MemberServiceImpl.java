package edu.the.joeun.myblog.service;

import edu.the.joeun.myblog.mapper.MemberMapper;
import edu.the.joeun.myblog.model.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Transactional
/*
해당 클래스 종료 시 까지 예외가 발생하지 않으면 commit
                      예외가 발생하면       rollback
*/
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<Member> selectMemberList() {
        // 유저 리스트 조회
        return memberMapper.selectMemberList();
    }

    @Override
    public String login(String memberEmail, String memberPassword, HttpSession session, Model model) {
        Member member = memberMapper.getMemberByEmail(memberEmail);

        if(member == null) {
            model.addAttribute("error", "not found email");
            return "login";
        }

        // 비밀번호 검증 (BCrypto...) 생략

        member.setMemberPassword(null);
        // sessionUtil
        return "redirect:/";  // 이 상태로 다시 메인페이지로 이동 = redirect: 어디로 다시 이동하자
    }

    public void logout(HttpSession session) {
        // 로그아웃 세션 추가
    }

    @Override
    public Member getMemberById(int memberId) {
        return memberMapper.getMemberById(memberId);
    }

    @Override
    public void saveMember(Member member) {
        // 암호화 설정, 비밀번호 저장
        // html -> controller 로 가져온 데이터 중에서 멤버 비밀번호만 가져오기
        // 가져온 비밀번호를 암호화 처리해서 다시 비밀번호 공간에 넣어놓기
        member.setMemberPassword(bCryptPasswordEncoder.encode(member.getMemberPassword()));
        // 모든 작업이 끝난 데이터를 DB에 저장하기
        memberMapper.saveMember(member);
    }
}
