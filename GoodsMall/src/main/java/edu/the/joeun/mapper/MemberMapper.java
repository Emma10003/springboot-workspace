package edu.the.joeun.mapper;

import edu.the.joeun.model.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<Member> getAllMembers();  // 모든 멤버 조회
    void insertMember(Member member);  // 멤버 등록
                                       // MemberMapper.xml에 작성된 id="insertMember"인 부분과 관련
}
