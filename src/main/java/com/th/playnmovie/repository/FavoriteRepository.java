package com.th.playnmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.th.playnmovie.model.Favorite;
import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.security.model.User;

import jakarta.transaction.Transactional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	
    boolean existsByUserAndItemIdAndType(User user, Long itemId, TypeEnum type);

	List<Favorite> findByUser(User user);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Favorite f WHERE f.user = :user AND f.itemId = :itemId AND f.type = :type")
	void unfavorite(@Param("user")User user,@Param("itemId")Long itemId, @Param("type")TypeEnum type);
}
