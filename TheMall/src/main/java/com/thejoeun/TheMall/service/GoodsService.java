package com.thejoeun.TheMall.service;

import com.thejoeun.TheMall.mapper.GoodsMapper;
import com.thejoeun.TheMall.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    // mapper의 GoodsMapper.java 에 작성했던 메서드와 동일한 명칭으로 설정할 것!
    public List<Goods> getAllGoods() {
        return goodsMapper.getAllGoods();
    }
}
