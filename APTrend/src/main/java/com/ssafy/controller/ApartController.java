package com.ssafy.controller;

import com.ssafy.dto.ApartDto;
import com.ssafy.dto.DealStatsDto;
import com.ssafy.dto.GptResponse;
import com.ssafy.dto.HotAptDto;
import com.ssafy.dto.QuestionPredict;
import com.ssafy.dto.QuestionRequest;
import com.ssafy.service.ApartService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartController {

    private final ApartService apartService;
	private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<List<ApartDto>> getAllApartments() {
        List<ApartDto> list = apartService.getAllApartments();
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/hot")
    public ResponseEntity<List<HotAptDto>> getHotApartments() {
        return ResponseEntity.ok(apartService.getHotApts());
    }
    @GetMapping("/monthly-deals")
    public ResponseEntity<List<DealStatsDto>> getMonthlyDeals(
        @RequestParam("roadAddress") String roadAddress,
        @RequestParam("aptName") String aptName
    ) {
        String id = roadAddress.strip() + "|" + aptName.strip();
        return ResponseEntity.ok(apartService.getMonthlyDeals(id));
    }
    @GetMapping("/predict")
    public ResponseEntity<GptResponse> AIPredict(
        @RequestParam("roadAddress") String roadAddress,
        @RequestParam("aptName") String aptName
    ) {
        String id = roadAddress.strip() + "|" + aptName.strip();
        List<DealStatsDto> ds = apartService.getMonthlyDeals(id);
        
        String fastApiUrl = "http://localhost:8000/predict";
		// 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        QuestionPredict request = new QuestionPredict(ds);
        HttpEntity<QuestionPredict> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<GptResponse> response = restTemplate.postForEntity(
                fastApiUrl, entity, GptResponse.class
        );
        
        System.out.println(response.getBody());
        return ResponseEntity.ok(response.getBody());
    }

}
