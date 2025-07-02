package com.th.playnmovie.mapper;

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.model.Movie;

public class MovieMapper {

	public static MovieDto toDto(Movie movie) {

		MovieDto movieDto = new MovieDto();
	
		movieDto.setId(movie.getId());
		movieDto.setTitle(movie.getTitle());
		movieDto.setReleaseDate(movie.getReleaseDate());
		movieDto.setSynopsis(movie.getSynopsis());
		movieDto.setImageUrl(movie.getImageUrl());
		movieDto.setGenres(movie.getGenres());
		return movieDto;
		
	}
	
	public static Movie toMovie(MovieDto dto) {
		Movie movie = new Movie();
		movie.setId(dto.getId());
		movie.setTitle(dto.getTitle());
		movie.setSynopsis(dto.getSynopsis());
		movie.setReleaseDate(dto.getReleaseDate());
		movie.setImageUrl("https://image.tmdb.org/t/p/w500" + dto.getImageUrl());
		movie.setGenres(dto.getGenres());

		return movie;
	}
	
}
