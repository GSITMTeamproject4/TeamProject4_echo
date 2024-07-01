package com.project.echoproject.dto.order;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private String orderNumber;
    private String buyerTel;
    private String buyerEmail;
    private String buyerAddr;
    private String buyerPostcode;
    private int totalAmount;
    private List<Item> items;
    private String buyerId;


    @Getter
    @Setter
    public static class Item {
        private String productId;
        private String productName;
        private int quantity;
    }
}
