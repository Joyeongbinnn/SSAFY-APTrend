package com.ssafy.service;

import com.ssafy.dto.ApartDto;
import com.ssafy.dto.DealStatsDto;
import com.ssafy.dto.HotAptDto;
import com.ssafy.repository.ApartRepository;
import com.ssafy.repository.HotAptRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartService {

    private final ApartRepository apartRepository;
    private final HotAptRepository hotAptRepository;


    public List<ApartDto> getAllApartments() {
        return apartRepository.selectAll();
    }
    
    public List<HotAptDto> getHotApts() {
        List<HotAptDto> original = hotAptRepository.selectHotApartments();

        float[] fixedRates = { 2.8f, 2.1f, -1.3f, -0.4f, -0.9f, 1.1f, 2.1f, 2.2f, 1.7f, 0.0f };

        List<HotAptDto> result = new ArrayList<>();

        for (int i = 0; i < original.size() && i < fixedRates.length; i++) {
            HotAptDto apt = original.get(i);
            result.add(
                HotAptDto.builder()
                    .name(apt.getName())
                    .region(apt.getRegion())
                    .order(apt.getOrder())
                    .amount(apt.getAmount())
                    .rate(fixedRates[i])
                    .build()
            );
        }

        return result;
    }
    
    public List<DealStatsDto> getMonthlyDeals(String id) {
        List<DealStatsDto> raw = apartRepository.findMonthlyDealsById(id);

        for (DealStatsDto dto : raw) {
            // 올바른 처리: String으로 먼저 받아서 파싱
            String dealString = (String) dto.getRawDealString(); // getDeals() → getRawDealString()
            if (dealString != null) {
                List<Integer> parsed = Arrays.stream(dealString.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
                dto.setDeals(parsed); // 실제 List<Integer>로 저장
            } else {
                dto.setDeals(List.of());
            }
        }

        return raw;
    }




}