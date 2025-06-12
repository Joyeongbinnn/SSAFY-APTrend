package com.ssafy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.dto.OAuth2UserDto;
import com.ssafy.dto.board.Board;
import com.ssafy.dto.review.ApartReview;
import com.ssafy.dto.review.Review;
import com.ssafy.dto.review.ReviewData;
import com.ssafy.dto.user.User;
import com.ssafy.repository.BoardRepository;
import com.ssafy.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository repo;
	
	public int createReview(Review review) {
		int result = repo.createReview(review);
		
		return result;
	}
	
	public int deleteReview(int userId, String apartId) {
		return repo.deleteReview(userId, apartId);
	}
	
	public int updateReview(Review review) {
		return repo.updateReview(review);
	}
	
	public ReviewData detailReview(int userId, String apartId) {
		return repo.detailReview(userId, apartId);
	}
	
	public void increaseApart(String apartId, Float rate) {
		repo.increaseApart(apartId, rate);
	}
	
	public void decreaseApart(String apartId, Float rate) {
		repo.decreaseApart(apartId, rate);
	}
	
	public List<ApartReview> apartReviewList(int page) {
		return repo.apartReviewList(page);
	}
	public List<ApartReview> popularApartReviewList(int page) {
		return repo.popularApartReviewList(page);
	}
	public List<ReviewData> apartDetailReviewList(String apartId) {
		return repo.apartDetailReviewList(apartId);
	}
	public ApartReview apartDetailReview(String apartId) {
		return repo.apartDetailReview(apartId);
	}
	public List<ReviewData> myReviewList(int userId) {
		return repo.myReviewList(userId);
	}
}
