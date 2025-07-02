package com.th.playnmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.th.playnmovie.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

	@Query(value = """
		    SELECT DISTINCT g.*
		    FROM game g
		    LEFT JOIN game_genres gg ON g.id = gg.game_id
		    WHERE (:title IS NULL OR REPLACE(LOWER(g.title), ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :title, '%')), ' ', ''))
		    AND (:#{#genres == null || #genres.isEmpty()} = true OR gg.genres IN (:genres))
		    """, nativeQuery = true)
	List<Game> findGames(@Param("title") String title, @Param("genres") List<String> genres);

}

