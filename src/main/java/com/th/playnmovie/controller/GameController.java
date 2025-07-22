package com.th.playnmovie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.service.GameService;

@RestController
@RequestMapping("/games")
public class GameController {

	@Autowired
	private GameService gameService;
	
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
