package com.project.echoproject.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {
    private String imp_uid;
    private String merchant_uid;
    private String status;
    private int amount;

    // 추가된 필드
    private String name;
    private String buyer_email;
    private String buyer_name;
    private String buyer_tel;
    private String buyer_addr;
    private String buyer_postcode;
}
