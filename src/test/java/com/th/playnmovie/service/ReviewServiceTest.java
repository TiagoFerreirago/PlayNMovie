package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.th.playnmovie.dto.ReviewDto;
import com.th.playnmovie.exception.ReviewNotFoundException;
import com.th.playnmovie.mapper.UserMapper;
import com.th.playnmovie.mock.ReviewMock;
import com.th.playnmovie.model.Review;
import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.repository.ReviewRepository;
import com.th.playnmovie.security.model.User;

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
		
		when(reviewRepository.findByItemIdAndType(1L, TypeEnum.MOVIE)).thenReturn(List.of(review, reviewTwo));
		
		var result = reviewService.findAllByItemAndType(1L, TypeEnum.MOVIE);
		
		assertNotNull(result);
		assertNotNull(result.size());
		
		ReviewDto dtoOne = result.get(0);
		assertNotNull(dtoOne.getId());
		assertNotNull(dtoOne.getUser());
		assertEquals(dtoOne.getId(), 1L);
		assertEquals(dtoOne.getComment(), "Test Comment 1");
		assertEquals(dtoOne.getItemId(), 3L);
		assertEquals(dtoOne.getNotice(), 9);
		assertEquals(dtoOne.getType(), TypeEnum.MOVIE);
		
		ReviewDto dtoTwo = result.get(1);
		
		assertNotNull(dtoTwo.getId());
		assertNotNull(dtoTwo.getUser());
		assertEquals(dtoTwo.getId(), 2L);
		assertEquals(dtoTwo.getComment(), "Test Comment 1");
		assertEquals(dtoTwo.getItemId(), 3L);
		assertEquals(dtoTwo.getNotice(), 9);
		assertEquals(dtoTwo.getType(), TypeEnum.MOVIE);
	
	}

	@Test
	void testCreateReview() {
		
		mockAuthenticatedUser(1L, "test");
		
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
		assertEquals(result.getNotice(), 9);
		assertEquals(result.getType(), TypeEnum.MOVIE);
		verify(reviewRepository).save(any(Review.class));
	}

	@Test
	void testUpdateReview() {

		User userMock = new User();
	    userMock.setId(1L);
	    userMock.setUsername("test");

	    Authentication authentication = mock(Authentication.class);
	    when(authentication.getPrincipal()).thenReturn(userMock);
	    
	    SecurityContext securityContext = mock(SecurityContext.class);
	    when(securityContext.getAuthentication()).thenReturn(authentication);
	    SecurityContextHolder.setContext(securityContext);

	    Review review = input.reviewMock(1L);
	    review.setId(1L);    
	    review.setUser(userMock);

	    Review persistenced = input.reviewMock(1L);
	    persistenced.setId(1L);    
	    persistenced.setUser(userMock);

	    ReviewDto dto = input.reviewMockDto(1L);
	    dto.setId(1L);
	    dto.setUser(UserMapper.tokenResponseVo(userMock));

	    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
	    when(reviewRepository.save(any(Review.class))).thenReturn(persistenced);

	    var result = reviewService.updateReview(dto);

	    assertNotNull(result.getId());
	    assertNotNull(result.getUser());
	    assertEquals(1L, result.getId());
	    assertEquals("Test Comment 1", result.getComment());
	    assertEquals(3L, result.getItemId());
	    assertEquals(11, result.getNotice());
	    assertEquals(TypeEnum.MOVIE, result.getType());
	}

	@Test
	void testDeleteReview() {

		User userMock = new User();
		userMock.setId(1L);
		userMock.setUsername("test");
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(userMock);
		
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		
		SecurityContextHolder.setContext(securityContext);
		
		Review review = input.reviewMock(1L);
		review.setId(1L);	
		review.setUser(userMock);

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

	 private void mockAuthenticatedUser(Long id, String username) {
		
		 User userMock = new User();
		 userMock.setId(id);
		 userMock.setUsername(username);
			
		 Authentication authentication = mock(Authentication.class);
		 lenient().when(authentication.getPrincipal()).thenReturn(userMock);
			
		 SecurityContext securityContext = mock(SecurityContext.class);
		 lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
			
		 SecurityContextHolder.setContext(securityContext);

	 }
}
