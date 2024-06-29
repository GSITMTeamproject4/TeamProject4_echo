package com.project.echoproject.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {
    private String productName;
    private int quantity;
    private int price;
}
