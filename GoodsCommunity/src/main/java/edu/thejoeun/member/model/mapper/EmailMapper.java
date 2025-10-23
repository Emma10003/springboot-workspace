package edu.thejoeun.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface EmailMapper {
    int updateAuthKey(Map<String, Object> map);

    int insertAUthKey(Map<String, Object> map);

    int checkAuthKey(Map<String, Object> map);
}
