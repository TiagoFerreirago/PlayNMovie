package com.th.playnmovie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.th.playnmovie.dto.ReviewDto;
import com.th.playnmovie.exception.ReviewNotFoundException;
import com.th.playnmovie.mapper.ReviewMapper;
import com.th.playnmovie.model.Review;
import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.repository.ReviewRepository;

@Service
public class ReviewService {

	private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public List<ReviewDto> findAllByItemAndType(Long itemId, TypeEnum type) {
        logger.debug("Searching reviews for itemId={} and type={}", itemId, type);

        List<Review> reviews = reviewRepository.findByItemIdAndType(itemId, type);
        if (reviews.isEmpty()) {
            logger.info("No reviews found for itemId={} and type={}", itemId, type);
            return List.of();
        }

        logger.debug("Found {} reviews for itemId={} and type={}", reviews.size(), itemId, type);
        return reviews.stream().map(ReviewMapper::toDto).collect(Collectors.toList());
    }

    public ReviewDto createReview(ReviewDto reviewDto) {
        logger.debug("Creating new review: itemId={}, userId={}", reviewDto.getItemId(), reviewDto.getUser().getId());

        Review review = ReviewMapper.fromDto(reviewDto);
        reviewRepository.save(review);

        logger.info("Review created with id={}", review.getId());
        return ReviewMapper.toDto(review);
    }

    public ReviewDto updateReview(ReviewDto reviewDto) {
        logger.debug("Updating review with id={}", reviewDto.getId());

        Review review = reviewRepository.findById(reviewDto.getId())
            .orElseThrow(() -> {
                logger.info("Review not found with id={}", reviewDto.getId());
                return new ReviewNotFoundException();
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

    public void deleteReview(Long id) {
        logger.debug("Deleting review with id={}", id);

        Review reviewExists = reviewRepository.findById(id).orElseThrow(() -> {
            logger.info("Review not found for deletion with id={}", id);
            return new ReviewNotFoundException();
        });

        reviewRepository.delete(reviewExists);
        logger.info("Review deleted with id={}", id);
    }
}
