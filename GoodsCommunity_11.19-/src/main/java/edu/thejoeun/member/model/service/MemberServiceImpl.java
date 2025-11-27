package edu.thejoeun.member.model.service;


import edu.thejoeun.common.util.SessionUtil;
import edu.thejoeun.member.model.dto.Member;
import edu.thejoeun.member.model.mapper.MemberMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MemberServiceImpl  implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public Member login(String memberEmail, String memberPassword) {
        Member member = memberMapper.getMemberByEmail(memberEmail);
        if(member == null) {
            return null;
        }
       if(!bCryptPasswordEncoder.matches(memberPassword, member.getMemberPassword())) {
            return null;
        }
        member.setMemberPassword(null);
            return member;
    }

    @Transactional  // 이거 작성 안 하면 저장 안됨!!
    @Override
    public void saveMember(Member member) {
        String originPW = member.getMemberPassword(); // 기존 클라이언트 비밀번호 가져오기
        String encodedPw = bCryptPasswordEncoder.encode(originPW); // 비밀번호 암호화
        member.setMemberPassword(encodedPw); // 암호화처리된 비밀번호로 교체

        // 교체된 비밀번호 포함해서 저장
        // 비밀번호 암호화해서 저장
        memberMapper.saveMember(member);
    }

    @Transactional
    @Override
    public Map<String, Object> updateMember(Member member, String currentPassword, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        try {
            // 현재 로그인된 사용자 정보 가져오기
            Member loginUser = SessionUtil.getLoginUser(session);
            if(loginUser == null) {
                res.put("success", false);
                res.put("message", "로그인이 필요합니다.");
                return res;
            }

            // DB에서 최신정보 가져오기
            // Member m = memberMapper.getMemberByEmail(member.getMemberEmail());
            Member m = memberMapper.getMemberByEmail(loginUser.getMemberEmail());
            log.info("💡 loginUser.getMemberEmail: {}", loginUser.getMemberEmail());
            log.info("💡 member.getMemberEmail: {}", member.getMemberEmail());

            // id -> where 절 조건으로 사용
            //  + 현재 비밀번호와 비밀번호 변경할 때 작성한 현재 비밀번호가 일치하는지 확인
            // 비밀번호 변경하는 경우
            if(currentPassword != null && !currentPassword.isEmpty()) {
                // 현재 비밀번호와 DB에 저장된 비밀번호가 일치하는지 확인
                if(!bCryptPasswordEncoder.matches(currentPassword, m.getMemberPassword())) {
                    res.put("success", false);
                    res.put("message", "wrongPassword");
                    log.warn("⚠️ 비밀번호 불일치 - 이메일 : {}", loginUser.getMemberEmail());
                    return res;
                }

                // 새 비밀번호 암호화 처리해서 저장할 수 있도록 로직 작성
                if(member.getMemberPassword() != null && !member.getMemberPassword().isEmpty()) {
                    String encodePw = bCryptPasswordEncoder.encode(member.getMemberPassword());
                    member.setMemberPassword(encodePw);
                }
            } else {
                // 비밀번호 변경하지 않은 경우 기존 비밀번호 유지
                member.setMemberPassword(m.getMemberPassword());
            }
            member.setMemberId(m.getMemberId());
            memberMapper.updateMember(member);

            // 수정된 db 내역으로 session 업데이트
            Member updatedMember = memberMapper.getMemberByEmail(member.getMemberEmail());
            updatedMember.setMemberPassword(null);
            SessionUtil.setLoginUser(session, updatedMember);

            res.put("success", true);
            res.put("message", "success");
            log.info("✅ 회원정보 수정 성공 - 이메일: {}", loginUser.getMemberEmail());

        } catch (Exception e){
            res.put("success", false);
            res.put("message", "회원정보 수정 중 오류가 발생했습니다.");
            log.error("❌ 회원정보 수정 실패 - 에러: {}", e.getMessage());
        }
        return res;
    }

    public Map<String, Object> loginProcess(String memberEmail, String memberPassword, HttpSession session) {
            Map<String, Object> res = new HashMap<>();
            Member m = login(memberEmail,memberPassword);
            if(m == null) {
                res.put("success",false);
                res.put("message","이메일 또는 비밀번호가 일치하지 않습니다.");
                log.warn("로그인 실패: {}", memberEmail);
                return  res;
            }
            SessionUtil.setLoginUser(session, m);
            res.put("success",true);
            res.put("message","로그인 성공");
            res.put("user",m);
            log.info("로그인 성공 : {}",m.getMemberEmail());
        return res;
        }

    /**
     * 로그아웃 처리
     * @param  session 로그인된 세션 정보 가져와서 로그아웃 처리 후
     * @return 처리결과 반환
     */
    public  Map<String, Object> logoutProcess(HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        SessionUtil.invalidateLoginUser(session);
        res.put("success",true);
        res.put("message","로그아웃 성공");

        return res;
        }

    /**
     * 로그인 상태확인
     * @param session 현재 세션을 가져온 후
     * @return 로그인 이 되어있으면 로그인이 되어있는 상태로 반환
     */
    public Map<String, Object> checkLoginStatus(HttpSession session) {
            Map<String, Object> res = new HashMap<>();
            Member loginUser = (Member) session.getAttribute("loginUser");

        if(loginUser == null) {
            res.put("loggedIn", false);
            res.put("user", null);
            log.debug("로그인 상태 확인: 로그인되지 않음");
        }else {
                res.put("loggedIn", true);
                res.put("user",loginUser);
                log.debug("로그인 상태 확인 : {}", loginUser.getMemberEmail());
            }
            return  res;
        }
    }
