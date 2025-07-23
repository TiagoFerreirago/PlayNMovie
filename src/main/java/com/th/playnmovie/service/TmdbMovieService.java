package com.th.playnmovie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.dto.TmdbResponseDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.exception.MovieNotFoundException;
import com.th.playnmovie.exception.TmdbApiException;
import com.th.playnmovie.mapper.MovieMapper;
import com.th.playnmovie.model.Movie;
import com.th.playnmovie.repository.MovieRepository;

@Service
public class TmdbMovieService {
	
	private static final Logger logger = LoggerFactory.getLogger(TmdbMovieService.class);

	@Value("${tmdb.api.token}")
	private String apiKey;
	
	@Value("${tmdb.api.url}")
	private String tmdbUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MovieRepository movieRepository;
	
	
	public List<MovieDto> getMoviesFromTmdbPage(int page) {
		if (page < 1) {
			logger.warn("Invalid page number: {}", page);
			throw new CustomizedBadRequestException("Page must be greater than or equal to 1.");
		}
		logger.info("Fetching movies from page {} on TMDb.", page);
			
		TmdbResponseDto response = fetchMoviesFromTmdb(page);
		
		return response.getResults();
	}
	
	public List<MovieDto> searchMoviesByTitleOrGenre(String title, List<String> genres) {
	    validateSearchParameters(title, genres);

	    List<MovieDto> matchedMovies = searchOnTmdb(title, genres);

	    if (matchedMovies.isEmpty()) {
	        logger.info("No movies found on TMDb. Attempting local search.");
	        matchedMovies = searchLocalMoviesByTitleOrGenre(title, genres);
	    }

	    return matchedMovies;
	}
	
	public List<MovieDto> searchLocalMoviesByTitleOrGenre(String title, List<String> genres) {
		logger.info("Searching locally for movies with title '{}' or genres '{}'.", title, genres);
		
		List<Movie> movies = movieRepository.findMovies(title, genres);
		if (movies.isEmpty()) {
			logger.warn("No movies found locally for the given criteria.");
		} else {
			logger.info("Found {} movies locally.", movies.size());
		}
		
		return movies.stream().map(MovieMapper::toDto).collect(Collectors.toList());
	}
	
	public TmdbResponseDto fetchMoviesFromTmdb(int page) {
		String url = tmdbUrl + "?api_key=" + apiKey + "&language=pt-BR&page=" + page;
		try {
			logger.debug("Calling TMDb API: {}", url);
			TmdbResponseDto response = restTemplate.getForObject(url, TmdbResponseDto.class);

			if (response == null || response.getResults() == null) {
				logger.error("Null or malformed response from TMDb API for page {}.", page);
				throw new MovieNotFoundException("Could not fetch data from TMDb.");
			}

			return response;

		}catch (HttpClientErrorException | HttpServerErrorException ex) {
		    logger.error("HTTP error when calling TMDb: {}", ex.getMessage(), ex);
		    throw new MovieNotFoundException("TMDb returned an error: " + ex.getStatusCode());
		} catch (RestClientException ex) {
		    logger.error("Client error when calling TMDb: {}", ex.getMessage(), ex);
		    throw new TmdbApiException("Unable to connect to TMDb.",ex);
		}
	}
	
	private boolean matchesCriteria(MovieDto movie, String title, List<String> genres) {
		
		boolean matchesTitle = (title == null || movie.getTitle().toLowerCase().contains(title.toLowerCase())); 
		boolean matchesGenre = (genres == null || genres.isEmpty()) || movie.getGenres().stream().anyMatch(genres::contains);
		return matchesTitle && matchesGenre;
				
	}
	
	private List<MovieDto> searchOnTmdb(String title, List<String> genres) {
	    TmdbResponseDto firstResponse = fetchMoviesFromTmdb(1);
	    int totalPages = firstResponse.getTotal_pages();
	    int maxPages = Math.min(totalPages, 10);

	    List<MovieDto> matchedMovies = new ArrayList<>();

	    for (int i = 1; i <= maxPages; i++) {
	        List<MovieDto> movies = getMoviesFromTmdbPage(i);
	        for (MovieDto movie : movies) {
	            if (matchesCriteria(movie, title, genres)) {
	                matchedMovies.add(movie);
	            }
	        }
	    }

	    return matchedMovies;
	}
	
	private void validateSearchParameters(String title, List<String> genres) {
		
		if ((title == null || title.trim().isEmpty()) &&
				(genres == null || genres.isEmpty())) {
				logger.warn("Empty search parameters: title and genres not provided.");
				throw new CustomizedBadRequestException("Provide at least a title or genre for search.");
			}
	}
}
