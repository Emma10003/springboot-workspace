package com.thejoeun.TheMall.mapper;

import com.thejoeun.TheMall.model.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*
Repository 의 경우
스프링부트 개발자 + 커뮤니티 개발자들이 만들어놓은 기능을
JPA 에서 가져와 사용한 것

Mapper 의 경우
resources/mappers/ 폴더 아래에
각 회사 개발자들이 작성한 SQL을 기반으로 움직이는 기능명칭들 작성
    mapper.xml 파일에서
    id로 작성한 명칭과 class Mapper.java 에서 작성하는 기능명칭이
    일치해야 xml 파일에 작성한 SQL 구문이 동작.
    <select id="getAllGoods" resultMap="goodsResultMap">
 */
@Mapper     // @Repository랑 동일한 위치
public interface GoodsMapper {

    // 모든 상품 조회
    // id="getAllGoods" 에서 작성한 SQL을 기반으로
    // 모든 데이터를 List 목록 형태로 가져올 것
    List<Goods> getAllGoods();
}
