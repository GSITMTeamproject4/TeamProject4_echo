package com.project.echoproject.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemSaveDTO {
    private Long productId;   // Product 엔티티의 ID 참조
    private int quantity;

}
