package com.th.playnmovie.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.th.playnmovie.dto.MovieDto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	@ElementCollection
	private List<String> genres;
	@Column(name = "release_date")
	private LocalDate releaseDate;
	@Column(name = "overview")
	private String synopsis;
	@Column(name = "poster_path")
	private String imageUrl;
	
	public Movie() {}
	
	public Movie(MovieDto dto) {
		this.title = dto.getTitle();
	    this.synopsis = dto.getSynopsis();
	    this.releaseDate = dto.getReleaseDate();
	    this.imageUrl = dto.getImageUrl();
	    this.genres = dto.getGenres(); 
	}
	
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

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
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
	}

	@Override
	public int hashCode() {
		return Objects.hash(genres, id, imageUrl, releaseDate, synopsis, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return Objects.equals(genres, other.genres) && Objects.equals(id, other.id)
				&& Objects.equals(imageUrl, other.imageUrl) && Objects.equals(releaseDate, other.releaseDate)
				&& Objects.equals(synopsis, other.synopsis) && Objects.equals(title, other.title);
	}


}
