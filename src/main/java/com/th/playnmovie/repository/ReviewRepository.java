package com.th.playnmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.th.playnmovie.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
