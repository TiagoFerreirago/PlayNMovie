package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.dto.TmdbResponseDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.exception.MovieNotFoundException;
import com.th.playnmovie.exception.TmdbApiException;
import com.th.playnmovie.mock.MovieMock;
import com.th.playnmovie.model.Movie;
import com.th.playnmovie.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class TmdbMovieServiceTest {

	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private MovieRepository movieRepository;
	
	@InjectMocks
	private TmdbMovieService tmdbService;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGetMovieForPage() {

		TmdbResponseDto responseDto = new TmdbResponseDto();
		responseDto.setResults(List.of(new MovieDto()));
		
		when(restTemplate.getForObject(anyString(), eq(TmdbResponseDto.class)))
		.thenReturn(responseDto);
		
		List<MovieDto> result = tmdbService.getMoviesFromTmdbPage(1);
		
		assertEquals(1, result.size());
	}

	@Test
	void testFindMovieByTitleOrGenre() {

		TmdbResponseDto tmdbResponse = new TmdbResponseDto();
		tmdbResponse.setResults(List.of());
		tmdbResponse.setTotal_pages(1);
		
		when(restTemplate.getForObject(contains("page=1"), eq(TmdbResponseDto.class)))
		.thenReturn(tmdbResponse);
		
		Movie movie = new Movie();
		movie.setTitle("Local Match");
		
		when(movieRepository.findMovies(any(), any())).thenReturn(List.of(movie));
		
		var result = tmdbService.searchMoviesByTitleOrGenre("test", List.of("Adventure"));
	
		assertEquals(1, result.size());
	}
	
	@Test
	void testFetchTmdbPage() {
	    MovieMock mock = new MovieMock();
	    MovieDto dto = mock.movieMockDto(1L);
	    dto.setId(1L);

	    TmdbResponseDto response = new TmdbResponseDto();
	    response.setResults(List.of(dto));

	    when(restTemplate.getForObject(anyString(), eq(TmdbResponseDto.class)))
	        .thenReturn(response);

	    TmdbResponseDto result = tmdbService.fetchMoviesFromTmdb(1);

	    assertNotNull(result);
	    assertNotNull(result.getResults().get(0).getId());
	    assertEquals(1, result.getResults().size());
	    assertEquals("Test Title Dto 1", result.getResults().get(0).getTitle());
	    assertEquals(List.of("Adventure"), result.getResults().get(0).getGenres());
	    assertEquals("Test Image Dto 1", result.getResults().get(0).getImageUrl());
	    assertEquals(LocalDate.of(2025, 6, 30), result.getResults().get(0).getReleaseDate());
	    assertEquals("Test Synopsis Dto 1", result.getResults().get(0).getSynopsis());
	}
	
	@Test
	void testFindMovieByTitleOrGenreMatchTitleNotGenre() {
	    MovieDto dto = new MovieDto();
	    dto.setTitle("Batman");
	    dto.setGenres(List.of("Action"));

	    TmdbResponseDto response = new TmdbResponseDto();
	    response.setResults(List.of(dto));
	    response.setTotal_pages(1);

	    when(restTemplate.getForObject(contains("page=1"), eq(TmdbResponseDto.class)))
	        .thenReturn(response);

	    List<MovieDto> result = tmdbService.searchMoviesByTitleOrGenre("Bat", List.of("Drama"));

	    assertEquals(0, result.size());
	}
	
	@Test
	void testGetMovieForPageInvalid() {
		Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
			tmdbService.getMoviesFromTmdbPage(0);
		});
		
		String expectedMessage = "Page must be greater than or equal to 1.";
		String messageReleased = exception.getMessage();
			    
		assertEquals(messageReleased, expectedMessage);
	}
	

	@Test
	void testFetchTmdbPageApiError() {
		when(restTemplate.getForObject(anyString(), eq(TmdbResponseDto.class)))
	    .thenThrow(new RestClientException("Error"));
	
		Exception exception = assertThrows(TmdbApiException.class, () -> { 
		tmdbService.fetchMoviesFromTmdb(1);
	});
		
	    String expectedMessage = "Unable to connect to TMDb.";
		String messageReleased = exception.getMessage();
		    
		assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testFetchTmdbPageReturnIsNull() {
	    when(restTemplate.getForObject(anyString(), eq(TmdbResponseDto.class)))
	        .thenReturn(null);

	    Exception exception = assertThrows(MovieNotFoundException.class, () -> {
	        tmdbService.fetchMoviesFromTmdb(1);
	    });
	    
	    String expectedMessage = "Could not fetch data from TMDb.";
		String messageReleased = exception.getMessage();
		    
		assertEquals(messageReleased, expectedMessage);
	}

	@Test
	void testFetchTmdbPageResultsIsNull() {
	    TmdbResponseDto response = new TmdbResponseDto();
	    response.setResults(null);

	    when(restTemplate.getForObject(anyString(), eq(TmdbResponseDto.class)))
	        .thenReturn(response);

	    Exception exception = assertThrows(MovieNotFoundException.class, () -> {
	        tmdbService.fetchMoviesFromTmdb(1);
	    });
	    
	    String expectedMessage = "Could not fetch data from TMDb.";
		String messageReleased = exception.getMessage();
		    
		assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testFindMovieByTitleOrGenreNoParams() {
	    Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
	        tmdbService.searchMoviesByTitleOrGenre(null, List.of());
	    });
	    
	    String expectedMessage = "Provide at least a title or genre for search.";
		String messageReleased = exception.getMessage();
		    
		assertEquals(messageReleased, expectedMessage);
	}

}
