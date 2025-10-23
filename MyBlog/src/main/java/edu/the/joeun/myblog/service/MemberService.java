package edu.the.joeun.myblog.service;

import edu.the.joeun.myblog.model.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

public interface MemberService {

    /**
     * 로그인을 위한 email 실행
     */

    /**
     * 모든 멤버 조회 로직을 완성하기
     * @return  반환데이터 : 멤버데이터 목록(=리스트)
     */
    List<Member> selectMemberList();

    /**
     * 로그인을 작업하기 위해
     * 이메일, 비밀번호 전달받고, session은 null 데이터 허용 (DB 존재하지 않기 때문)
     * model도 null 데이터 허용 (DB에 존재하지 않기 때문)
     * @param memberEmail
     * @param memberPassword
     * @param session
     * @param model
     * @return  -> 로그인된 유저 정보 반환
     */
    String login(String memberEmail, String memberPassword, HttpSession session, Model model);

    /**
     * 관리자가 멤버 상세보기를 진행할 경우
     * 특정 멤버의 id값을 이용해서 db에서 멤버정보를 가져온다.
     * @param memberId  html 에서 멤버 id를 클릭했을 때 id값 가져오기
     * @return  id값으로 해당되는 데이터 member 형태로 클라이언트에 전달
     */
    Member getMemberById(int memberId);

    /**
     * html -> js -> controller 로 전달받은 유저의 모든 정보를
     * DB에 저장하고, 반환값은 존재하지 않으며 전달 유무만 클라이언트에게 확인시킴
     * @param member
     */
    void saveMember(Member member);
}
