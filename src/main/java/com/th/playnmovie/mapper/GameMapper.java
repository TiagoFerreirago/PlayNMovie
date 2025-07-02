package com.th.playnmovie.mapper;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.model.Game;

public class GameMapper {

	 public static Game fromDto(GameDto dto) {
	        Game game = new Game();
	        game.setId(dto.getId());
	        game.setTitle(dto.getTitle());
	        game.setGenres(dto.getGenres());
	        game.setReleaseDate(dto.getReleaseDate());
	        game.setSynopsis(dto.getSynopsis());
	        game.setUrl(dto.getUrl());
	        return game;
	    }

	    public static GameDto toDto(Game game) {
	        return new GameDto(
	                game.getId(),
	                game.getTitle(),
	                game.getGenres(),
	                game.getReleaseDate(),
	                game.getSynopsis(),
	                game.getUrl()
	        );
	    }
}
