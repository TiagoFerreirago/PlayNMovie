package com.th.playnmovie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.th.playnmovie.dto.ReviewDto;
import com.th.playnmovie.mapper.ReviewMapper;
import com.th.playnmovie.model.Review;
import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.repository.ReviewRepository;

@Service
public class ReviewService {

	private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public List<ReviewDto> findAllByItemAndType(Long itemId, TypeEnum type) throws NotFoundException {
		logger.info("Fetching reviews for itemId={} and type={}", itemId, type);
		
		List<Review> reviews = reviewRepository.findByItemIdAndType(itemId, type);
		if(reviews.isEmpty()) {
			logger.warn("No reviews found for itemId={} and type={}", itemId, type);
			throw new NotFoundException();
		}
		
		logger.info("Found {} reviews for itemId={} and type={}", reviews.size(), itemId, type);
		return reviews.stream().map(p -> ReviewMapper.toDto(p)).collect(Collectors.toList());
		
	}
	
	public ReviewDto create(ReviewDto reviewDto) {
		logger.info("Creating new review for itemId={}, userId={}", reviewDto.getItemId(), reviewDto.getUser().getId());

		Review review = ReviewMapper.fromDto(reviewDto);
		reviewRepository.save(review);
		
		logger.info("Review created with id={}", review.getId());
		return ReviewMapper.toDto(review);
	}
	
	public ReviewDto update(ReviewDto reviewDto) throws NotFoundException {
		logger.info("Updating review with id={}", reviewDto.getId());

		Review review = reviewRepository.findById(reviewDto.getId())
			.orElseThrow(() -> {
				logger.warn("Review not found with id={}", reviewDto.getId());
				return new NotFoundException();
			});

		review.setComment(reviewDto.getComment());
		review.setItemId(reviewDto.getItemId());
		review.setNotice(reviewDto.getNotice());
		review.setType(reviewDto.getType());
		review.setUser(reviewDto.getUser());

		reviewRepository.save(review);

		logger.info("Review updated with id={}", review.getId());
		return ReviewMapper.toDto(review);
	}
	
	public void delete(Long id) throws NotFoundException {
		logger.info("Deleting review with id={}", id);

		Review reviewExists = reviewRepository.findById(id).orElseThrow(() -> {
			logger.warn("Review not found for deletion with id={}", id);
			return new NotFoundException();
		});

		reviewRepository.delete(reviewExists);
		logger.info("Review deleted with id={}", id);
	}
}
