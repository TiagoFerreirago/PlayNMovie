package com.th.playnmovie.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.exception.GameNotFoundException;
import com.th.playnmovie.exception.response.ErrorMessages;
import com.th.playnmovie.mapper.GameMapper;
import com.th.playnmovie.model.Game;
import com.th.playnmovie.repository.GameRepository;
import com.th.playnmovie.util.ValidationUtils;

import reactor.core.publisher.Flux;

@Service
public class GameService {
	
	Logger logger = LoggerFactory.getLogger(GameService.class);
	
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private IgdbClient igdbClient;

	public GameDto createGame(GameDto dto){
		
		if (dto == null)
		        throw new CustomizedBadRequestException(ErrorMessages.GAME_DATA_REQUIRED);
		
	    logger.info("createGame called with title: {}", dto.getTitle());

		validateNewGame(dto);
		
	    Game game = GameMapper.fromDto(dto);
	    gameRepository.save(game);
	    return GameMapper.toDto(game);
	}
	
	public GameDto updateGame(GameDto dto){
		logger.info("Updating game with ID: {}, title: {}", dto.getId(), dto.getTitle());

        validateUpdateGame(dto);

	    Game game = gameRepository.findById(dto.getId())
	            .orElseThrow(() -> new GameNotFoundException());

	    game.setGenres(dto.getGenres());
	    game.setReleaseDate(dto.getReleaseDate());
	    game.setSynopsis(dto.getSynopsis());
	    game.setTitle(dto.getTitle());
	    game.setUrl(dto.getUrl());

	    gameRepository.save(game);
	    return GameMapper.toDto(game);
	}

	
	public void deleteGame(Long id){
		if (id == null) throw new CustomizedBadRequestException(ErrorMessages.ID_REQUIRED);
		logger.info("Deleting game with id: {}", id);
		 
		Game game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("Game with ID " + id + " not found."));
		gameRepository.delete(game);
	}
	
	 public Flux<GameDto> getGamesFromDb() {
	        return Flux.fromIterable(gameRepository.findAll())
	                   .map(p -> GameMapper.toDto(p));
	 }
	
	 public Flux<GameDto> getGamesFromDbAndApi(int page) {
		 Flux<GameDto> fromDb = Flux.fromIterable(gameRepository.findAll()).map(p -> GameMapper.toDto(p));
		
		 Flux<GameDto> fromApi = igdbClient.getAllGames(page);
		 
		 return Flux.concat(fromDb, fromApi); 
	 }
	 
	 private void validateNewGame(GameDto dto) {
	       if (dto == null) throw new CustomizedBadRequestException(ErrorMessages.GAME_DATA_REQUIRED);
	       if (ValidationUtils.isBlank(dto.getTitle())) throw new CustomizedBadRequestException(ErrorMessages.TITLE_REQUIRED);
	       if (ValidationUtils.isEmpty(dto.getGenres())) throw new CustomizedBadRequestException(ErrorMessages.GENRE_REQUIRED);
	 }

	 private void validateUpdateGame(GameDto dto) {
	        if (dto == null || dto.getId() == null)
	            throw new CustomizedBadRequestException(ErrorMessages.ID_REQUIRED);
	        validateNewGame(dto); 
	 }

	
}
