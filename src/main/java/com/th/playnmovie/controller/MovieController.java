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

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.service.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieService movieService;
	

	@PostMapping
	public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto){
		return ResponseEntity.status(HttpStatus.CREATED).body((movieService.createMovie(movieDto)));
	}
	
	@PutMapping
	public ResponseEntity<MovieDto> updateMovie(@RequestBody MovieDto movieDto){
		return ResponseEntity.ok().body(movieService.updateMovie(movieDto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMovieById(@PathVariable("id") Long id){
		movieService.deleteMovieById(id);
		return ResponseEntity.noContent().build();
	}

}
