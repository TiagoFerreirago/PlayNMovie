package com.th.playnmovie.mock;

import com.th.playnmovie.dto.ReviewDto;
import com.th.playnmovie.model.Review;
import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.security.model.User;
import com.th.playnmovie.security.vo.UserVo;

public class ReviewMock {

	public Review reviewMock() {
		
		return reviewMock(1L);
	}

	public ReviewDto reviewMockDto() {

		return reviewMockDto(1L);
	}
	
	public Review reviewMock(Long i) {

		Review review = new Review();
		review.setComment("Test Comment "+i);
		review.setId(i);
		review.setItemId(2L+i);
		review.setNotice(8 + Integer.parseInt(i.toString()));
		review.setType(TypeEnum.FILME);
		review.setUser(new User());
		
		return review;
	}

	public ReviewDto reviewMockDto(Long i) {
		
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setComment("Test Comment "+i);
		reviewDto.setId(i);
		reviewDto.setItemId(2L+i);
		reviewDto.setNotice(10 + Integer.parseInt(i.toString()));
		reviewDto.setType(TypeEnum.FILME);
		reviewDto.setUser(new UserVo());

		return reviewDto;
	}
}
