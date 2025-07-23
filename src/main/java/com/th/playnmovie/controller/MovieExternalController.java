package com.th.playnmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.service.TmdbMovieService;

@RestController
@RequestMapping("/movies-external")
public class MovieExternalController {

	@Autowired
	private TmdbMovieService tmdbService;
	
	@GetMapping("/popular")
	public ResponseEntity<List<MovieDto>> getMoviesFromTmdbPage(@RequestParam(defaultValue = "0") int page){
		
		List<MovieDto> movieDtos  = tmdbService.getMoviesFromTmdbPage(page);
		return ResponseEntity.ok().body(movieDtos );
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<MovieDto>> searchMoviesByTitleOrGenre(
			@RequestParam(value ="title", required = false)String title, 
			@RequestParam(value ="genres", required = false)List<String> genres){
		List<MovieDto> movieDtos  = tmdbService.searchMoviesByTitleOrGenre(title, genres);
		return ResponseEntity.ok().body(movieDtos);
	}


}
