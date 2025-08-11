package com.th.playnmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.th.playnmovie.model.Review;
import com.th.playnmovie.model.TypeEnum;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	
	List<Review>findByItemIdAndType(Long itemId, TypeEnum type);
	List<Review>findByItemId(Long itemId);
	List<Review>findByType(TypeEnum type);
}
