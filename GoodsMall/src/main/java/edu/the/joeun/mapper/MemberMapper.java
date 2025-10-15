package edu.the.joeun.mapper;

import edu.the.joeun.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    // mybatis xml 에서 작성한 SQL을 가져올 id와 기능명칭 작성
    // resources/mappers/memberMapper.xml 파일에서
    // id값이 saveMember 인 SQL 구문을 가져와 보유하고 있는 형태를 띄울 것

    void saveMember(Member member);  // 멤버 등록
                                       // MemberMapper.xml에 작성된 id="insertMember"인 부분과 관련
}
