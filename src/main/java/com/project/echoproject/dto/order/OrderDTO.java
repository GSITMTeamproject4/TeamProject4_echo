package com.project.echoproject.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private String orderNumber;
    private int totalAmount;
    private BuyerDTO buyer;
    private List<ItemDTO> items;
    private LocalDateTime orderDate;  // 추가된 필드

}
