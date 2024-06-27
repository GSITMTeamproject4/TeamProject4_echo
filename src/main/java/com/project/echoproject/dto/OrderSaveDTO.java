package com.project.echoproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderSaveDTO {
    private String orderNumber;
    private String buyerTel;
    private String buyerAddr;
    private String buyerPostcode;
    private int totalAmount;
    private List<OrderItemSaveDTO> items;
    private String buyerId; // 추가

}
