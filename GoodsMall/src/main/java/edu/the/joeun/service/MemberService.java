package edu.the.joeun.service;

import edu.the.joeun.mapper.MemberMapper;
import edu.the.joeun.model.Member;
import edu.the.joeun.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    // 비밀번호 암호화
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * Controller에서 Mapper의 getAllMembers() 메서드를 호출하기 위해 Service에서 getAllMembers() 메서드를 만든 것
     * @return
     */
    public List<Member> getAllMembers(){
        return memberMapper.getAllMembers(); // service 에서 mapper의 메서드 호출
    }

    public void insertMembers(Member member) {
        String userPassword = member.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(userPassword);

        member.setPassword(encodedPassword);  // 암호화된 패스워드를 Member 객체 형식인 member 변수의
                                              // password 변수에 담고
        memberMapper.insertMember(member);    // mapper의 insertMember 메서드를 호출해
                                              // 암호화된 패스워드가 담긴 member 변수를 매개변수로 전달
    }
}
