package com.th.playnmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.th.playnmovie.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	@Query(value = """
		    SELECT DISTINCT m.*
		    FROM movie m
		    WHERE (:title IS NULL OR REPLACE(LOWER(m.title), ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :title, '%')), ' ', ''))
		    AND (
		        (:genres IS NULL)
		        OR EXISTS (
		            SELECT 1 FROM movie_genres mg
		            WHERE mg.movie_id = m.id AND mg.genres IN (:genres)
		        )
		    )
		    """, nativeQuery = true)
	List<Movie> findMovies(@Param("title") String title, @Param("genres") List<String> genres);

}
