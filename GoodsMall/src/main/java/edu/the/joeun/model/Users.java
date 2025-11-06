package edu.the.joeun.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
z
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    private String email;
    private String password;
    private String role;
    private String create_at;  // SQL 에서 가입일자나 유저 정보 수정일자를 
    private String update_at;  // 관리자가 확인하고자 할 때 사용하기 위해 넣어놓은 변수명 
}
