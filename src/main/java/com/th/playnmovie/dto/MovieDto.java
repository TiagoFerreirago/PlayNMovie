package com.th.playnmovie.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.th.playnmovie.util.ConvertMovieData;

public class MovieDto {

	 private Long id;
	 private String title;
	 @JsonProperty("overview")
	 private String synopsis;
	 @JsonProperty("release_date")
	 private LocalDate releaseDate;
	 @JsonProperty("poster_path")
	 private String imageUrl;
	
	 private List<Integer> genreIds;
	 private List<String> genres;

	public MovieDto() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public List<String> getGenres() {
		return genres;
	}
  
	public void setGenres(List<String> genres) {
		this.genres = genres;
		if (genres != null) {
	       this.genreIds = genres.stream()
	            .map(ConvertMovieData::convertGenreNameToId)
	            .collect(Collectors.toList());
	     } else {
	           this.genreIds = new ArrayList<>();
	     }
				
	}
	@JsonIgnore
	public List<Integer> getGenreIds() {
		return genreIds;
	}
	
	@JsonProperty("genre_ids")
	public void setGenreIds(List<Integer> genreIds) {
		this.genreIds = genreIds;
		this.genres = genreIds.stream()
		        .map(ConvertMovieData::convertGenreIdToName)
		        .collect(Collectors.toList());
	}
}
