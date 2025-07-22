package com.th.playnmovie.mock;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.dto.IgdbReleaseDateDto;
import com.th.playnmovie.model.Game;
import com.th.playnmovie.model.GenreEnum;

public class GameMock {

	
	public Game gameMock() {
		return gameMock(1);
		
	}
	
	public GameDto gameMockDto() {
		return gameMockDto(1);
		
	}
	
	public Game gameMock(long i) {
		
		Game game = new Game();
		game.setGenres(List.of(GenreEnum.RPG.getName()));
		game.setId(i);
		game.setReleaseDate(LocalDate.of(2025, Month.FEBRUARY, 20));
		game.setSynopsis("synopsis "+i);
		game.setTitle("title "+i);
		game.setUrl("url "+i);
		return game;
	}
	
	public GameDto gameMockDto(long i) {
		
		long epoch = LocalDate.of(2025,6,24).atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
		GameDto gameDto = new GameDto();
		gameDto.setGenresFromIds(List.of(12));
		gameDto.setId(i);
		gameDto.setReleaseDateFromJson(List.of(new IgdbReleaseDateDto(epoch)));
		gameDto.setSynopsis("synopsis dto "+i);
		gameDto.setTitle("title dto "+i);
		gameDto.setUrl("url dto "+i);
		return gameDto;
	}
}
