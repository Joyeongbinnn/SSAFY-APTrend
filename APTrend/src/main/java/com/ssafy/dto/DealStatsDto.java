package com.ssafy.dto;

import lombok.Data;
import java.util.List;

@Data
public class DealStatsDto {
    private String month;
    private int average;

    // 아래 필드 2개 필요
    private String rawDealString;      // → MyBatis에서 GROUP_CONCAT로 주입됨
    private List<Integer> deals;       // → 파싱 후 사용
}
