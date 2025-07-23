package com.th.playnmovie.mock;

import java.time.LocalDate;
import java.util.List;

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.model.Movie;

public class MovieMock {

	public Movie movieMock() {
		return movieMock(1L);
	}
	
	public MovieDto movieMockDto() {
		return movieMockDto(1L);
	}

	public MovieDto movieMockDto(Long d) {
		
		MovieDto dto = new MovieDto();
		dto.setId(d);
		dto.setImageUrl("Test Image Dto " + d);
		dto.setReleaseDate(LocalDate.of(2025, 6, 30));
		dto.setSynopsis("Test Synopsis Dto "+ d);
		dto.setTitle("Test Title Dto " + d);
		dto.setGenres(List.of("Aventura"));
		
		return dto;
	}

	public Movie movieMock(Long i) {

		Movie movie = new Movie();
		movie.setId(i);
		movie.setImageUrl("Test Image " + i);
		movie.setReleaseDate(LocalDate.of(2025, 6, 22));
		movie.setSynopsis("Test Synopsis "+ i);
		movie.setTitle("Test Title " + i);
		movie.setGenres(List.of("Aventura"));
		
		return movie;
	}
}
