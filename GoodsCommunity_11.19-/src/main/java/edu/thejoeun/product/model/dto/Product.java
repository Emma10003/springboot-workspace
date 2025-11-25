package edu.thejoeun.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String productName;
    private String productCode;
    private String category;
    private int price;
    private int stockQuantity;
    private String description;
    private String manufacturer;
    private String imageUrl;
    private String isActive;
    /*
    private boolean isActive;

    원래 MySQL 에서는 boolean isActive로 받아왔지만,
    Oracle 에는 boolean 자료형이 없어 Char 형태로 저장되므로 String 으로 받아온다.
    (주로 'Y', 'N' 형태를 주로 사용함.)

    만약에 isActive 를 boolean 타입으로 제공한다면
    serviceImpl 에서
    isActive 가 'Y'일 경우 boolean true 로 변환하여 프론트엔드에 전달하도록
    로직을 작성할 수 있으나,
    그보다는 private String isActive 로 데이터를 전달받아 활용하는 것이 용이함.
    */
    private String createdAt;
    private String updatedAt;
}
