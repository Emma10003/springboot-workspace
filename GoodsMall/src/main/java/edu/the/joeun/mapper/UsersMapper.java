package edu.the.joeun.mapper;

import edu.the.joeun.model.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersMapper {
    // UsersMapper.xml 에서 데이터를 가져와서 Service에 전달해주는 역할
    
    List<Users> getAllUsers();  // 모든 유저 조회 -> List 자료형 사용
    // 여기에서 사용되는 메서드명은 UsersMapper.xml 에서 작성한 select 혹은 insert뒤의 id.

    /*
    insert 의 경우 void 와 int 둘 다 가능

        int - return  :  저장된 데이터의 수 반환
            여러 개의 데이터를 한 번에 저장할 때,
            몇 개의 데이터가 저장되었고 몇 개의 데이터가 저장되지 않았는지
            클라이언트에게 전달하고자 할 때 사용

        void          : 저장결과 유무 확인할 수 있음
            단일 데이터를 저장하고, 데이터가 몇 개 저장되었는지
            클라이언트에게 전달하지 않을 때 주로 사용 (단순 저장)

    select, update, delete 도 위의 insert와 비슷하게
        상황에 따라 int를 사용하기도 하고, void를 사용하기도 함.
    - 몇 개의 데이터를 조회(검색)했는지 확인하고자 할 때 int를 주로 사용
    - 몇 개의 데이터를 수정, 삭제했는지 확인하고자 할 때 int를 주로 사용
    - 단순 조회, 수정, 삭제의 경우 void 나 User 와 같은 자료형을 활용하기도 함.

     => 개발자가 원하는 결과 상황에 따라 자료형은 void, int User 와 같은 자료형 사용
     */

    // 단순 저장 확인용 -> void(=리턴값 없음) 선택
    // Tip) AI나 인터넷에서 아래와 같은 방식을 추천할 경우
    //      쿼리가 동작하긴 하지만 유지보수가 어려워 사용을 지양하는 형태
    //      @INSERT("INSERT INTO user (name, email, role) VALUES(#{name}, #{email}, #{role})")
    void insertUsers(Users users);  // 유저 등록
}
