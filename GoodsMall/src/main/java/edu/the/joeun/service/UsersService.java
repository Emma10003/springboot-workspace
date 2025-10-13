package edu.the.joeun.service;

import edu.the.joeun.mapper.UsersMapper;
import edu.the.joeun.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersMapper usersMapper;

    public List<Users> getAllUsers(){
        return usersMapper.getAllUsers();
    }


    public void insertUsers(Users users){
        /**
         * 비밀번호 암호화 같은 복합 작업 진행하는 공간
         * 프로필 사진을 폴더에 저장하고 폴더 경로도 DB에 저장 가능
         */
        // users.setRole("USER");
        //  └─> HTML 에서 클라이언트가 작성 설정을 하는 것이 아니라
        //      개발자의 회사에서 클라이언트 정보를 강제적으로 기본값 설정해줄 때
        //      추가 로직을 작성할 수 있음
        usersMapper.insertUsers(users);
    }
}
