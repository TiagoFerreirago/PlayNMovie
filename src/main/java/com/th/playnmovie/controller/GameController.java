package com.th.playnmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.service.GameService;
import com.th.playnmovie.service.IgdbService;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private IgdbService igdbService;
	@Autowired
	private GameService gameService;
	
	@GetMapping
	public ResponseEntity<Flux<GameDto>> getAllGames(@RequestParam(value = "page", defaultValue = "0") int page,
													@RequestParam(defaultValue = "both")String source){
		switch(source) {
		
		case "db":
			return ResponseEntity.ok().body(gameService.getGamesFromDb());
		
		case "api":
			return ResponseEntity.ok().body(igdbService.getAllGames(page));
			
		default:
			return ResponseEntity.ok().body(gameService.getGames(page));
			
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<Flux<GameDto>> findByNameOrGenres(@RequestParam(value ="name", required = false) String name,@RequestParam(value = "genres", required =false) List<String> genres){
		Flux<GameDto> game = igdbService.findByNameOrGenres(name, genres);
		return ResponseEntity.ok().body(game);
	}
	
	@PostMapping
	public ResponseEntity<GameDto> createGame(@RequestBody GameDto gameDto){
		GameDto responseDto = gameService.createGame(gameDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}
	
	@PutMapping
	public ResponseEntity<GameDto> updateGame(@RequestBody GameDto gameDto){
	    GameDto responseDto = gameService.updateGame(gameDto);
	    return ResponseEntity.ok().body(responseDto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteGame(@PathVariable Long id){
		gameService.deleteGame(id);
		return ResponseEntity.noContent().build();
	}
}
