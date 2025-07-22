package com.th.playnmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.th.playnmovie.dto.FavoriteDto;
import com.th.playnmovie.security.model.User;
import com.th.playnmovie.service.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	
	@Autowired
	private FavoriteService favoriteService;

	@PostMapping
	public ResponseEntity<FavoriteDto> favorite(@RequestBody FavoriteDto favoriteDto){
		User user = new User();
		user.setId(favoriteDto.getUser().getId());
		
		FavoriteDto dto = favoriteService.favorite(favoriteDto);
		return ResponseEntity.ok().body(dto);
	}
	@DeleteMapping
	public ResponseEntity<Void> unfavorite(@RequestBody FavoriteDto favoriteDto){
		User user = new User();
		user.setId(favoriteDto.getUser().getId());
		
		favoriteService.unFavorite(favoriteDto);
		return ResponseEntity.noContent().build();
	}
	@GetMapping
	public ResponseEntity<List<FavoriteDto>> findAll(){
		
		List<FavoriteDto> favoritesDto = favoriteService.findAll();
		return ResponseEntity.ok().body(favoritesDto);
	}
}
