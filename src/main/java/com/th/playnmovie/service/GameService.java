package com.th.playnmovie.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.exception.GameNotFoundException;
import com.th.playnmovie.mapper.GameMapper;
import com.th.playnmovie.model.Game;
import com.th.playnmovie.repository.GameRepository;

import reactor.core.publisher.Flux;

@Service
public class GameService {
	
	Logger logger = LoggerFactory.getLogger(GameService.class);
	
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private IgdbService igdbService;

	public GameDto createGame(GameDto dto){
	    logger.info("createGame chamado com título: {}", dto.getTitle());

		validateNewGame(dto);
		
	    Game game = GameMapper.fromDto(dto);
	    gameRepository.save(game);
	    return GameMapper.toDto(game);
	}
	
	public GameDto updateGame(GameDto dto){
        logger.info("updateGame called with dto: {}", dto);
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
		if (id == null) throw new CustomizedBadRequestException("ID obrigatório.");
		logger.info("Deleting game with id: {}", id);
		
		Game game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("Jogo com ID " + id + " não encontrado."));
		gameRepository.delete(game);
	}
	
	 public Flux<GameDto> getGamesFromDb() {
	        return Flux.fromIterable(gameRepository.findAll())
	                   .map(p -> GameMapper.toDto(p));
	 }
	
	 public Flux<GameDto> getGames(int page) {
		 Flux<GameDto> fromDb = Flux.fromIterable(gameRepository.findAll()).map(p -> GameMapper.toDto(p));
		
		 Flux<GameDto> fromApi = igdbService.getAllGames(page);
		 
		 return Flux.merge(fromDb, fromApi); 
	 }
	 
	  private void validateNewGame(GameDto dto) {
	        if (dto == null) throw new CustomizedBadRequestException("Dados do jogo não fornecidos.");
	        if (isBlank(dto.getTitle())) throw new CustomizedBadRequestException("Título obrigatório.");
	        if (isEmpty(dto.getGenres())) throw new CustomizedBadRequestException("Gêneros obrigatórios.");
	    }

	  private void validateUpdateGame(GameDto dto) {
	        if (dto == null || dto.getId() == null)
	            throw new CustomizedBadRequestException("ID do jogo é obrigatório.");
	        validateNewGame(dto); 
	 }

	 private boolean isBlank(String str) {
	        return str == null || str.trim().isEmpty();
	 }

	private boolean isEmpty(List<?> list) {
	        return list == null || list.isEmpty();
	}
}
