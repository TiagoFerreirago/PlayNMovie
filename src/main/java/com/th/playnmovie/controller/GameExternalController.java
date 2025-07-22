package com.th.playnmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.service.GameService;
import com.th.playnmovie.service.IgdbClient;
import com.th.playnmovie.service.IgdbService;

@RestController
@RequestMapping("/games-external")
public class GameExternalController {

	@Autowired
	private IgdbService igdbService;
	@Autowired
	private GameService gameService;
	@Autowired
	private IgdbClient igdbClient;
	
	@GetMapping
	public ResponseEntity<Flux<GameDto>> getGamesFromDbAndApi(@RequestParam(value = "page", defaultValue = "0") int page,
													@RequestParam(defaultValue = "both")String source){
		switch(source) {
		
		case "db":
			return ResponseEntity.ok().body(gameService.getGamesFromDb());
		
		case "api":
			return ResponseEntity.ok().body(igdbClient.getAllGames(page));
			
		default:
			return ResponseEntity.ok().body(gameService.getGamesFromDbAndApi(page));
			
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<Flux<GameDto>> findByNameOrGenres(@RequestParam(value ="name", required = false) String name,@RequestParam(value = "genres", required =false) List<String> genres){
		Flux<GameDto> game = igdbService.findByNameOrGenres(name, genres);
		return ResponseEntity.ok().body(game);
	}
	

}
