package edu.the.joeun.service;

import edu.the.joeun.mapper.MemberMapper;
import edu.the.joeun.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    // public List<Member> getAllMembers(){
    //     return memberMapper.getAllMembers(); // service 에서 mapper의 메서드 호출
    // }

    // void : 멤버가 몇 명 저장되었는지 체크하지 않고, 멤버 저장 유무만 추후에 SQL에서 전달받는 형태
    public void saveMember(Member member) {

        // 비밀번호 암호화

        // 1단계 : 기존에 유저가 html에서 작성한 비밀번호를 가져와서 변수공간에 담아둔다.
        String originPw = member.getPassword();  // 기존 비밀번호

        // 2단계 : 유저가 작성한 비밀번호 암호화 처리해 변수공간에 담아둔다.
        String newPw = bCryptPasswordEncoder.encode(originPw);

        // 3단계 : 새로 만든 비밀번호를 member 객체 내부에 있는 password 변수에 다시 담아놓는다.
        member.setPassword(newPw);          // 암호화된 패스워드를 Member 객체 형식인 member 변수의
                                            // password 변수에 담고
        memberMapper.saveMember(member);    // mapper의 insertMember 메서드를 호출해
                                            // 암호화된 패스워드가 담긴 member 변수를 매개변수로 전달

        // 위에서 변수명칭으로 나누어놓은 코드 로직을 아래처럼 한 줄로 작성해도 된다.
        // 나누는 이유 -> 개발을 진행할 때 System.out.println() 으로
        //                코드 로직이 개발자가 원하는 형태로 진행되고 있는지 확인하기 위해
        //                변수 명칭으로 나누어서 작성
        // member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
    }
}
