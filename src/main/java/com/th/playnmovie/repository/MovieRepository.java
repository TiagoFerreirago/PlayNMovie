package com.th.playnmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.th.playnmovie.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	@Query("SELECT m FROM Movie m WHERE :title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title ,'%'))) AND "+
	":gender IS NULL OR LOWER(m.gender) = LOWER(:gender))")
	List<Movie>findMovies(@Param("title")String title, @Param("gender")String gender);
}
