package com.th.playnmovie.service;

import java.nio.file.FileAlreadyExistsException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.th.playnmovie.dto.MovieDto;
import com.th.playnmovie.mapper.MovieMapper;
import com.th.playnmovie.model.Movie;
import com.th.playnmovie.repository.MovieRepository;

import jakarta.persistence.EntityExistsException;

@Service
public class MovieService {

	private Logger logger = LoggerFactory.getLogger(MovieService.class);

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private TmdbService tmdbService;

	public MovieDto createMovie(MovieDto movieDto) throws NotFoundException {
		validateMovieDto(movieDto);

		logger.info("Checking if the movie '{}' already exists before creating.", movieDto.getTitle());
		List<MovieDto> found = tmdbService.findMovieByTitleOrGenre(movieDto.getTitle(), movieDto.getGenres());

		if (!found.isEmpty()) {
			logger.warn("Movie '{}' already exists in the system.", movieDto.getTitle());
			throw new EntityExistsException();
		}

		Movie movie = MovieMapper.toMovie(movieDto);
		movieRepository.save(movie);
		logger.info("Movie '{}' successfully created.", movieDto.getTitle());
		return MovieMapper.toDto(movie);
	}

	public MovieDto updateMovie(MovieDto movieDto) throws NotFoundException {

		if (movieDto == null || movieDto.getId() == null) {
			logger.warn("Attempt to update movie with null ID.");
			throw new IllegalArgumentException("Movie ID is required for update.");
		}

		validateMovieDto(movieDto);

		logger.info("Updating movie with ID: {}", movieDto.getId());
		Movie movieEntity = movieRepository.findById(movieDto.getId())
				.orElseThrow(() -> {
					logger.error("Movie with ID {} not found for update.", movieDto.getId());
					return new NotFoundException();
				});

		movieEntity.setGenres(movieDto.getGenres());
		movieEntity.setImageUrl(movieDto.getImageUrl());
		movieEntity.setReleaseDate(movieDto.getReleaseDate());
		movieEntity.setSynopsis(movieDto.getSynopsis());
		movieEntity.setTitle(movieDto.getTitle());
		movieRepository.save(movieEntity);

		logger.info("Movie with ID {} successfully updated.", movieDto.getId());
		return MovieMapper.toDto(movieEntity);
	}

	public void deleteMovieById(Long id) throws NotFoundException {

		if (id == null) {
			logger.warn("Attempt to delete movie with null ID.");
			throw new IllegalArgumentException("Movie ID is required for deletion.");
		}

		logger.info("Deleting movie with ID: {}", id);

		Movie movieEntity = movieRepository.findById(id)
				.orElseThrow(() -> {
					logger.error("Movie with ID {} not found for deletion.", id);
					return new NotFoundException();
				});

		movieRepository.delete(movieEntity);
		logger.info("Movie with ID {} successfully deleted.", id);
	}

	private void validateMovieDto(MovieDto movieDto) {
		if (movieDto == null) {
			logger.warn("Movie DTO is null.");
			throw new IllegalArgumentException("Movie data is required.");
		}

		if (movieDto.getTitle() == null || movieDto.getTitle().trim().isEmpty()) {
			logger.warn("Invalid movie title: '{}'", movieDto.getTitle());
			throw new IllegalArgumentException("Movie title is required.");
		}

		if (movieDto.getGenres() == null || movieDto.getGenres().isEmpty()) {
			logger.warn("Genres not provided for movie '{}'", movieDto.getTitle());
			throw new IllegalArgumentException("At least one genre must be provided.");
		}
	}
}

