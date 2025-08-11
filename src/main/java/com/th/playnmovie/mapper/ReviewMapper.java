package com.th.playnmovie.mapper;

import com.th.playnmovie.dto.ReviewDto;
import com.th.playnmovie.model.Review;

public class ReviewMapper {
	
	public static ReviewDto toDto(Review review) {
		ReviewDto dto = new ReviewDto();
		dto.setComment(review.getComment());
		dto.setId(review.getId());
		dto.setItemId(review.getItemId());
		dto.setNotice(review.getNotice());
		dto.setType(review.getType());
		dto.setUser(UserMapper.tokenResponseVo(review.getUser()));
		return dto;
	}
	
	public static Review fromDto(ReviewDto dto) {
		Review review = new Review();
		review.setComment(dto.getComment());
		review.setId(dto.getId());
		review.setItemId(dto.getItemId());
		review.setNotice(dto.getNotice());
		review.setType(dto.getType());
		review.setUser(UserMapper.tokenResponse(dto.getUser()));
		return review;
	}
}