package edu.thejoeun.member.model.service;

import edu.thejoeun.member.model.dto.Member;
import edu.thejoeun.member.model.mapper.MemberMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    /**
     * Autowired 형태로 변경해 <br>
     * config 에서 관리할 것
     */
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * MemberService 에서 작성한 기능명칭(매개변수 자료형, 개수)가 <br>
     * MemberServiceImpl 과 일치하지 않으면 @Override 된 상태가 아님 <br>
     * 명칭만 똑같이 썼을 뿐
     * @param memberEmail       html -> js -> controller api/endpoint 로 가져온 이메일
     * @param memberPassword    html -> js -> controller api/endpoint 로 가져온 비밀번호
     * @param session           유저 정보가 DB에 조회되면 session 부여
     * @param model             유저 정보가 조회되면 model 세팅
     * @return
     */
    @Override
    public Member login(String memberEmail, String memberPassword) {
        Member member = memberMapper.getMemberByEmail(memberEmail);
        // email 로 DB에서 조회되는 데이터가 0개이면 null (=사용자가 존재하지 않는다면)
        if(member == null){
            return null;
        }

        // 비밀번호 일치하지 않으면 null
        if(!bCryptPasswordEncoder.matches(member.getMemberPassword(), memberPassword)) {
            return null;
        }

        // 이메일도 존재하고, 비밀번호도 일치하면
        member.setMemberPassword(null);  // 비밀번호 제거한 상태로 유저 정보를 controller 에 전달
        return member; // 멤버에 대한 모든 정보를 컨트롤러에 전달
    }
}
