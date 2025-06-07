package com.th.playnmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.th.playnmovie.model.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
