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

    /**
     * 모든 유저 정보 조회
     * 
     * @return SQL에서 가져온 유저 목록(List<Users>)을 반환하여
     *         반환된 상품 목록 조회 가능
     */
    public List<Users> getAllUsers(){
        return usersMapper.getAllUsers();
    }

    /**
     * 전달받은 유저 정보를 DB에 저장
     * @param users
     */
    public void insertUsers(Users users){
        usersMapper.insertUsers(users);
    }
}
