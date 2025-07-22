package com.th.playnmovie.controller;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

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

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.service.MovieService;
import com.th.playnmovie.service.TmdbService;

@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private TmdbService tmdbService;
	@Autowired
	private MovieService movieService;
	
	@GetMapping("/popular/{page}")
	public ResponseEntity<List<MovieDto>> getPopularMoviesByPage(@PathVariable("page")int page){
		
		List<MovieDto> movieDto = tmdbService.getMovieForPage(page);
		return ResponseEntity.ok().body(movieDto);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<MovieDto>> findMovieByTitleOrGenre(@RequestParam(value ="title", required = false)String title, @RequestParam(value ="genrer", required = false)List<String> genrer) throws AccountNotFoundException {
		List<MovieDto> movieDto = tmdbService.findMovieByTitleOrGenre(title, genrer);
		return ResponseEntity.ok().body(movieDto);
	}
	
	@PostMapping
	public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto){
		return ResponseEntity.status(HttpStatus.CREATED).body((movieService.createMovie(movieDto)));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto movieDto){
		return ResponseEntity.ok().body(movieService.updateMovie(movieDto));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteMovieById(@PathVariable("id") Long id){
		movieService.deleteMovieById(id);
		return ResponseEntity.noContent().build();
	}

}
