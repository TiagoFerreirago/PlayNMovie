package com.th.playnmovie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.dto.TmdbResponseDto;
import com.th.playnmovie.exception.MovieNotFoundException;
import com.th.playnmovie.mapper.MovieMapper;
import com.th.playnmovie.model.Movie;
import com.th.playnmovie.repository.MovieRepository;

@Service
public class TmdbService {
	
	Logger logger = LoggerFactory.getLogger(TmdbService.class);

	@Value("${tmdb.api.token}")
	private  String apiKey;
	
	private final String tmdbUrl = "https://api.themoviedb.org/3/movie/popular";
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private MovieRepository movieRepository;
	
	
	public List<MovieDto> getMovieForPage(int page) {
		if (page < 1) {
			logger.warn("Invalid page number: {}", page);
			throw new IllegalArgumentException("Page must be greater than or equal to 1.");
		}
		logger.info("Fetching movies from page {} on TMDb.", page);
			
		TmdbResponseDto response = fetchTmdbPage(page);
		
		return response.getResults();
	}
	
	public List<MovieDto> findMovieByTitleOrGenre(String title, List<String> genres){
		logger.info("Starting movie search by title '{}' or genres '{}'.", title, genres);
		
		if ((title == null || title.trim().isEmpty()) &&
			(genres == null || genres.isEmpty())) {
			logger.warn("Empty search parameters: title and genres not provided.");
			throw new IllegalArgumentException("Provide at least a title or genre for search.");
		}
        
		TmdbResponseDto firstResponse = fetchTmdbPage(1);
		int totalPages = firstResponse.getTotal_pages();
		int maxPages = Math.min(totalPages, 10);
		
		List<MovieDto> matchedMovies = new ArrayList<>();

		for (int i = 1; i <= maxPages; i++) {
			List<MovieDto> findMovies = getMovieForPage(i);
			for (MovieDto m : findMovies) {
				boolean matchesTitle = (title == null || m.getTitle().toLowerCase().contains(title.toLowerCase())); 
				boolean matchesGenre = (genres == null || genres.isEmpty()) || m.getGenres().stream().anyMatch(genres::contains);
				if (matchesTitle && matchesGenre) {
					matchedMovies.add(m);
				}
			}
		}
		if (matchedMovies.isEmpty()) {
			logger.info("No movies found on TMDb. Attempting local search.");
			matchedMovies = findLocalMoviesByTitleOrGenre(title, genres);
		}
		
		return matchedMovies;
	}
	
	public List<MovieDto> findLocalMoviesByTitleOrGenre(String title, List<String> genres) {
		logger.info("Searching locally for movies with title '{}' or genres '{}'.", title, genres);
		
		List<Movie> movies = movieRepository.findMovies(title, genres);
		if (movies.isEmpty()) {
			logger.warn("No movies found locally for the given criteria.");
		} else {
			logger.info("Found {} movies locally.", movies.size());
		}
		
		return movies.stream().map(MovieMapper::toDto).collect(Collectors.toList());
	}
	
	public TmdbResponseDto fetchTmdbPage(int page) {
		String url = tmdbUrl + "?api_key=" + apiKey + "&language=pt-BR&page=" + page;
		try {
			logger.debug("Calling TMDb API: {}", url);
			TmdbResponseDto response = restTemplate.getForObject(url, TmdbResponseDto.class);

			if (response == null || response.getResults() == null) {
				logger.error("Null or malformed response from TMDb API for page {}.", page);
				throw new MovieNotFoundException("Could not fetch data from TMDb.");
			}

			return response;

		} catch (Exception ex) {
			logger.error("Error calling TMDb API: {}", ex.getMessage(), ex);
			throw new MovieNotFoundException("Error fetching data from TMDb.");
		}
	}
}
