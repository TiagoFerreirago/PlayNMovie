package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.th.playnmovie.dto.ReviewDto;
import com.th.playnmovie.exception.ReviewNotFoundException;
import com.th.playnmovie.mock.ReviewMock;
import com.th.playnmovie.model.Review;
import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.repository.ReviewRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

	private ReviewMock input;
	@Mock
	private ReviewRepository reviewRepository;
	@InjectMocks
	private ReviewService reviewService;
	
	@BeforeEach
	void setUp() throws Exception {
		input = new ReviewMock();
	}

	@Test
	void testFindAllByItemAndType() {

		Review review = input.reviewMock(1L);
		review.setId(1L);
		
		Review reviewTwo = input.reviewMock(1L);
		reviewTwo.setId(2L);
		
		when(reviewRepository.findByItemIdAndType(1L, TypeEnum.FILME)).thenReturn(List.of(review, reviewTwo));
		
		var result = reviewService.findAllByItemAndType(1L, TypeEnum.FILME);
		
		assertNotNull(result);
		assertNotNull(result.size());
		
		ReviewDto dtoOne = result.get(0);
		assertNotNull(dtoOne.getId());
		assertNotNull(dtoOne.getUser());
		assertEquals(dtoOne.getId(), 1L);
		assertEquals(dtoOne.getComment(), "Test Comment 1");
		assertEquals(dtoOne.getItemId(), 3L);
		assertEquals(dtoOne.getNotice(), 9);
		assertEquals(dtoOne.getType(), TypeEnum.FILME);
		
		ReviewDto dtoTwo = result.get(1);
		
		assertNotNull(dtoTwo.getId());
		assertNotNull(dtoTwo.getUser());
		assertEquals(dtoTwo.getId(), 2L);
		assertEquals(dtoTwo.getComment(), "Test Comment 1");
		assertEquals(dtoTwo.getItemId(), 3L);
		assertEquals(dtoTwo.getNotice(), 9);
		assertEquals(dtoTwo.getType(), TypeEnum.FILME);
	
	}

	@Test
	void testCreateReview() {
		
		Review review = input.reviewMock(1L);
		review.setId(1L);	
		
		ReviewDto dto = input.reviewMockDto(1L);
		dto.setId(1L);
		
		when(reviewRepository.save(any(Review.class))).thenReturn(review);
		
		var result = reviewService.createReview(dto);
		
		assertNotNull(result.getId());
		assertNotNull(result.getUser());
		assertEquals(result.getId(), 1L);
		assertEquals(result.getComment(), "Test Comment 1");
		assertEquals(result.getItemId(), 3L);
		assertEquals(result.getNotice(), 11);
		assertEquals(result.getType(), TypeEnum.FILME);
		verify(reviewRepository).save(any(Review.class));
	}

	@Test
	void testUpdateReview() {

		Review review = input.reviewMock(1L);
		review.setId(1L);	
		
		Review persistenced = input.reviewMock(1L);
		review.setId(1L);	
		
		ReviewDto dto = input.reviewMockDto(1L);
		dto.setId(1L);
		
		when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
		when(reviewRepository.save(review)).thenReturn(persistenced);
		
		var result = reviewService.updateReview(dto);
		
		assertNotNull(result.getId());
		assertNotNull(result.getUser());
		assertEquals(result.getId(), 1L);
		assertEquals(result.getComment(), "Test Comment 1");
		assertEquals(result.getItemId(), 3L);
		assertEquals(result.getNotice(), 11);
		assertEquals(result.getType(), TypeEnum.FILME);
	
	}

	@Test
	void testDeleteReview() {

		Review review = input.reviewMock(1L);
		review.setId(1L);	

		when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
		
		reviewService.deleteReview(1L);
		
		verify(reviewRepository).delete(review);
	}
	
	@Test
	void testUpdateReviewWhenReviewNotFound() {
	    ReviewDto dto = input.reviewMockDto(1L);
	    dto.setId(999L);
	    
	    when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

	    Exception exception = assertThrows(ReviewNotFoundException.class, () -> {
	        reviewService.updateReview(dto);
	    });
	    
	    String expectedMessage = "The review was not found!";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testDeleteReviewNotFound() {
	    when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

	    Exception exception = assertThrows(ReviewNotFoundException.class, () -> {
	        reviewService.deleteReview(999L);
	    });
	    
	    String expectedMessage = "The review was not found!";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}




}
