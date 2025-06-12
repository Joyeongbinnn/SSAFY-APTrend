package com.ssafy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApartDto {
    private String id;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String sigungu;
    private Float price;     // optional: 현재 시세
    private Float rate;      // optional: 전월 대비 상승률
}