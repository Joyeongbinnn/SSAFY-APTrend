package com.ssafy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotAptDto {
    private String region;       // 지역
    private String name;       // 아파트 이름
    private int order;         // 거래 수
    private String amount;     // 거래 가격 (₩12.8억)
    private float rate;        // 증감률 (%)
}
