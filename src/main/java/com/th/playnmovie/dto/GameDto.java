package com.th.playnmovie.dto;

import java.time.LocalDate;
import java.util.Objects;

import com.th.playnmovie.model.Game;

public class GameDto {

	private Long id;
	private String title;
	private String gender;
	private LocalDate releaseDate;
	private String synopsis;
	private String imageUrl;
	
	public GameDto() {}

	public GameDto(Game game) {
		this.id = game.getId();
		this.title = game.getTitle();
		this.gender = game.getGender();
		this.releaseDate = game.getReleaseDate();
		this.synopsis = game.getSynopsis();
		this.imageUrl = game.getImageUrl();
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
		GameDto other = (GameDto) obj;
		return Objects.equals(gender, other.gender) && Objects.equals(id, other.id)
				&& Objects.equals(imageUrl, other.imageUrl) && Objects.equals(releaseDate, other.releaseDate)
				&& Objects.equals(synopsis, other.synopsis) && Objects.equals(title, other.title);
	}
	
}
