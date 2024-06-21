package com.project.echoproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OpenApiGasDTO {
// 오픈 api 서버에서 가져온 데이터를 받는 dto 생성
    private String useYm; // 사용년월
    private String platPlc; //대지 위치(도로명 주소 전 주소)
    private String sigunguCd; //시군구코드(행정표준코드)
    private String bjdongCd; //법정동코드(행정표준코드) - 동 이름 출력
    private Double useQty; //가스 사용량
}
