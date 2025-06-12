package com.ssafy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dto.OAuth2UserDto;
import com.ssafy.dto.PageDto;
import com.ssafy.dto.review.ApartReview;
import com.ssafy.dto.review.Review;
import com.ssafy.dto.review.ReviewData;
import com.ssafy.dto.user.User;
import com.ssafy.jwt.CustomOAuth2User;
import com.ssafy.service.ReviewService;
import com.ssafy.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService service;
	private final UserService userService;
	
	@PostMapping("/apart")
	public ResponseEntity<List<ApartReview>> getApartReviewList(@RequestBody PageDto page) {
		List<ApartReview> list = service.apartReviewList((page.getPage()-1)*10);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/popular")
	public ResponseEntity<List<ApartReview>> getPopularApartReviewList(@RequestBody PageDto page) {
		List<ApartReview> list = service.popularApartReviewList((page.getPage()-1)*10);
		System.out.println(list);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/myreview")
	public ResponseEntity<List<ReviewData>> myReviewList(@AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		List<ReviewData> list = service.myReviewList(userId);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/apart/{apartId}")
	public ResponseEntity<List<ReviewData>> getApartDetailReviewList(@PathVariable String apartId) {
		System.out.println(apartId);
		List<ReviewData> list = service.apartDetailReviewList(apartId);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/apartdetail/{apartId}")
	public ResponseEntity<ApartReview> getApartDetailReview(@PathVariable String apartId) {
		ApartReview apart = service.apartDetailReview(apartId);
		return ResponseEntity.ok(apart);
	}
	
	@GetMapping("/{apartId}")
	public ResponseEntity<ReviewData> getReviewDetail(@PathVariable String apartId, @AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		System.out.println(apartId);
		ReviewData detail = service.detailReview(userId,apartId);
		return ResponseEntity.ok(detail);
	}
	
	@PatchMapping("/{apartId}")
	public ResponseEntity<Void> updateReview(@PathVariable String apartId, @RequestBody Review review, @AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		review.setUserId(userId);
		review.setApartId(apartId);
		int result = service.updateReview(review);
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("/create")
	public ResponseEntity<Void> createReview(@RequestBody Review review, @AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		review.setUserId(userId);
		int result = service.createReview(review);
		service.increaseApart(review.getApartId(), review.getRate());
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@DeleteMapping("/{apartId}")
	public ResponseEntity<Void> deleteReview(@PathVariable String apartId, @AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		ReviewData review = service.detailReview(userId, apartId);
		service.decreaseApart(apartId, review.getRate());
		int result = service.deleteReview(userId, apartId);
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
