package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.exception.MovieAlreadyExistsException;
import com.th.playnmovie.exception.MovieNotFoundException;
import com.th.playnmovie.mock.MovieMock;
import com.th.playnmovie.model.Movie;
import com.th.playnmovie.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

	@Mock
	private MovieRepository movieRepository;
	@InjectMocks
	private MovieService movieService;
	@Mock
	private TmdbMovieService tmdbService;

	
	private MovieMock input;
	
	@BeforeEach
	void setUp() throws Exception {
		
		input = new MovieMock();
	}

	@Test
	void testCreateMovie() {

		Movie movie = input.movieMock(1L);
		movie.setId(1L);
		
		Movie persistenced = movie;
		persistenced.setId(1L);
		
		MovieDto dto = input.movieMockDto(1L);
		dto.setId(1L);
		
		when(tmdbService.searchMoviesByTitleOrGenre(dto.getTitle(), dto.getGenres()))
	    .thenReturn(List.of());
		when(movieRepository.save(any(Movie.class))).thenReturn(persistenced);

		var result = movieService.createMovie(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(result.getImageUrl(), "https://image.tmdb.org/t/p/w500Test Image Dto 1");
		assertEquals(result.getReleaseDate(), LocalDate.of(2025, 6, 30));
		assertEquals(result.getSynopsis(), "Test Synopsis Dto 1");
		assertEquals(result.getTitle(), "Test Title Dto 1");
		assertEquals(result.getGenres(), List.of("Aventura"));
		verify(tmdbService).searchMoviesByTitleOrGenre(dto.getTitle(),dto.getGenres());
		verify(movieRepository).save(any(Movie.class));
		
	}

	@Test
	void testUpdateMovie() {

		Movie movie = input.movieMock(1L);
		movie.setId(1L);
		
		Movie persistenced = movie;
		persistenced.setId(1L);
		
		MovieDto dto = input.movieMockDto(1L);
		dto.setId(1L);
		
		when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
		when(movieRepository.save(movie)).thenReturn(persistenced);
		
		var result = movieService.updateMovie(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(result.getImageUrl(), "Test Image Dto 1");
		assertEquals(result.getReleaseDate(), LocalDate.of(2025, 6, 30));
		assertEquals(result.getSynopsis(), "Test Synopsis Dto 1");
		assertEquals(result.getTitle(), "Test Title Dto 1");
		assertEquals(result.getGenres(), List.of("Aventura"));
		verify(movieRepository).findById(1L);
		verify(movieRepository).save(movie);
	}

	@Test
	void testDeleteMovieById() {

	
		Movie movie = input.movieMock(1L);
		movie.setId(1L);
		
		when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
		
		movieService.deleteMovieById(1L);
		verify(movieRepository).findById(1L);
		verify(movieRepository).delete(movie);
	}
	
	@Test
	void testCreateMovieAlreadyExists(){
	    MovieDto dto = input.movieMockDto(1L);
	    when(tmdbService.searchMoviesByTitleOrGenre(dto.getTitle(), dto.getGenres()))
	        .thenReturn(List.of(dto));

	    Exception exception = assertThrows(MovieAlreadyExistsException.class, () -> {
	        movieService.createMovie(dto);
	    });
	    
	    String expectedMessage = "Movie already exists in the system.";
		String messageReleased = exception.getMessage();
		    
		assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testUpdateMovieNullDto() {
	    Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
	        movieService.updateMovie(null);
	    });
	    
	    String expectedMessage = "Movie ID is required for update.";
		String messageReleased = exception.getMessage();
		    
		assertEquals(messageReleased, expectedMessage);
	}

	@Test
	void testUpdateMovieIdNotFound() {
	    MovieDto dto = input.movieMockDto(99L);
	    when(movieRepository.findById(99L)).thenReturn(Optional.empty());

	    Exception exception = assertThrows(MovieNotFoundException.class, () -> {
	        movieService.updateMovie(dto);
	    });
	    
	    String expectedMessage = "The requested movie was not found in the system.";
		String messageReleased = exception.getMessage();
		    
		assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testUpdateMovieNull() {
	   Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
	        movieService.updateMovie(null);
	    });
	   
	   String expectedMessage = "Movie ID is required for update.";
	   String messageReleased = exception.getMessage();
	    
	   assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testUpdateMovieNullId() {
		
		MovieDto dto = input.movieMockDto();
		dto.setId(null);
		
		Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
			movieService.updateMovie(dto);
		});
		
		String expectedMessage = "Movie ID is required for update.";
		String messageReleased = exception.getMessage();
		
		assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testDeleteMovieByIdNullId() {
	    Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
	        movieService.deleteMovieById(null);
	    });
	    
	    String expectedMessage = "Movie ID is required for deletion.";
		String messageReleased = exception.getMessage();
		
		assertEquals(messageReleased, expectedMessage);
	}

	@Test
	void testDeleteMovieById_IdNotFound() {
	    when(movieRepository.findById(99L)).thenReturn(Optional.empty());

	    Exception exception = assertThrows(MovieNotFoundException.class, () -> {
	        movieService.deleteMovieById(99L);
	    });
	    
	    String expectedMessage = "The requested movie was not found in the system.";
		String messageReleased = exception.getMessage();
		
		assertEquals(messageReleased, expectedMessage);
	}

}