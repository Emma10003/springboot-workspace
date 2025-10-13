package edu.the.joeun.service;

import edu.the.joeun.mapper.UsersMapper;
import edu.the.joeun.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersMapper usersMapper;

    // 다른 개발자가 만든 비밀번호 암호화 생성할 수 있는 객체
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public List<Users> getAllUsers(){
        return usersMapper.getAllUsers();
    }


    public void insertUsers(Users users){
        /**
         * 비밀번호 암호화 같은 복합 작업 진행하는 공간
         * 프로필 사진을 폴더에 저장하고 폴더 경로도 DB에 저장 가능
         */
        // 비밀번호 암호화 코드
        String 유저작성패스워드 = users.getPassword();
        String 암호화처리패스워드 = bCryptPasswordEncoder.encode(유저작성패스워드);
        
        users.setPassword(암호화처리패스워드);  // 암호화 처리된 패스워드로 교체하여 user DB에 저장하도록 설정
        usersMapper.insertUsers(users);

    }


    // users.setRole("USER");
    //  └─> HTML 에서 클라이언트가 작성 설정을 하는 것이 아니라
    //      개발자의 회사에서 클라이언트 정보를 강제적으로 기본값 설정해줄 때
    //      추가 로직을 작성할 수 있음
}
