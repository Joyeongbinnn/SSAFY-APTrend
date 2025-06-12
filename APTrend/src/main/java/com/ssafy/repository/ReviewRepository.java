package com.ssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.dto.OAuth2UserDto;
import com.ssafy.dto.review.ApartReview;
import com.ssafy.dto.review.Review;
import com.ssafy.dto.review.ReviewData;
import com.ssafy.dto.user.User;

@Repository
@Mapper
public interface ReviewRepository {
	int createReview(Review review);
	int deleteReview(int userId, String apartId);
	int updateReview(Review review);
	
	void increaseApart(String apartId, Float rate);
	void decreaseApart(String apartId, Float rate);
	
	ReviewData detailReview(int userId, String apartId);
	ApartReview apartDetailReview(String apartId);
	List<ApartReview> apartReviewList(int page);
	List<ApartReview> popularApartReviewList(int page);
	List<ReviewData> apartDetailReviewList(String apartId);
	List<ReviewData> myReviewList(int userId);
}
