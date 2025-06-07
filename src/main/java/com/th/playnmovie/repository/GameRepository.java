package com.th.playnmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.th.playnmovie.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

	@Query("SELECT g FROM Game g WHERE (:title IS NULL OR LOWER(g.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "+
	"(:gender IS NULL OR LOWER(g.gender) = LOWER(:gender))")
	List<Game>findGames(@Param("title")String title, @Param("gender")String gender);
}
