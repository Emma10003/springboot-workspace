package edu.the.joeun.mapper;

import edu.the.joeun.model.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersMapper {
    List<Users> getAllUsers();  // 모든 유저 조회
    
    void insertUsers(Users users);  // 유저 등록
}
