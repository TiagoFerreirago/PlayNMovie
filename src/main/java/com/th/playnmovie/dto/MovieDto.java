package com.th.playnmovie.dto;

import java.time.LocalDate;
import java.util.Objects;

import com.th.playnmovie.model.Movie;

public class MovieDto {

	private Long id;
	private String title;
	private String gender;
	private LocalDate releaseDate;
	private String synopsis;
	private String imageUrl;
	
	public MovieDto() {}
	
	public MovieDto(Movie movie) {
		
		this.id = movie.getId();
		this.title = movie.getTitle();
		this.gender = movie.getGender();
		this.releaseDate = movie.getReleaseDate();
		this.synopsis = movie.getSynopsis();
		this.imageUrl = movie.getImageUrl();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getGender() {
		return gender;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gender, id, imageUrl, releaseDate, synopsis, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieDto other = (MovieDto) obj;
		return Objects.equals(gender, other.gender) && Objects.equals(id, other.id)
				&& Objects.equals(imageUrl, other.imageUrl) && Objects.equals(releaseDate, other.releaseDate)
				&& Objects.equals(synopsis, other.synopsis) && Objects.equals(title, other.title);
	}
	
	
}
