package com.th.playnmovie.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.th.playnmovie.model.GenreEnum;

@JsonPropertyOrder({"id","title","releaseDate","url","genres","synopsis"})
public class GameDto {

	private Long id;
	@JsonProperty("name")
	private String title;
	private List<String> genres;
	private LocalDate releaseDate;
	@JsonProperty("summary")
	private String synopsis;
	private String url;
	
	@JsonIgnore
	private List<Integer> genreIds;
	@JsonIgnore
	private List<IgdbReleaseDateDto> releaseDates;
	
	public GameDto() {}

	public GameDto(Long id, String title, List<String> genres, LocalDate releaseDate, String synopsis, String url) {
	    this.id = id;
	    this.title = title;
	    this.genres = genres;
	    this.releaseDate = releaseDate;
	    this.synopsis = synopsis;
	    this.url = url;
	}
	
	@JsonProperty("genres")
	public void setGenresFromIds(List<Integer> genreIds) {
	    this.genreIds = genreIds;
	    this.genres = genreIds.stream()
                .map(g -> GenreEnum.fromId(g).getName())
                .collect(Collectors.toList());
	}
	
	@JsonProperty("cover")
	 public void unpackCoverFromJson(CoverDto cover) {
	     if (cover != null && cover.getUrl() != null) {
	          this.url = cover.getUrl();
	     }
	 }
	
	@JsonProperty("release_dates")
	public void  setReleaseDateFromJson(List<IgdbReleaseDateDto> releaseDates) {
		this.releaseDates = releaseDates;
	      if (releaseDates != null && !releaseDates.isEmpty()) {
	          this.releaseDate = releaseDates.get(0).getLocalDate();
	      }
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

	public List<Integer> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(List<Integer> genreIds) {
		this.genreIds = genreIds;
	}

	
	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public List<String> getGenres() {
		return genres;
	}

	public List<IgdbReleaseDateDto> getReleaseDates() {
		return releaseDates;
	}
	

	@Override
	public String toString() {
		return "GameDto [id=" + id + ", title=" + title + ", genres=" + genres + ", releaseDate=" + releaseDate
				+ ", synopsis=" + synopsis + ", url=" + url + ", genreIds=" + genreIds + ", releaseDates="
				+ releaseDates + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(genreIds, genres, id, releaseDate, releaseDates, synopsis, title, url);
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
		return Objects.equals(genreIds, other.genreIds) && Objects.equals(genres, other.genres)
				&& Objects.equals(id, other.id) && Objects.equals(releaseDate, other.releaseDate)
				&& Objects.equals(releaseDates, other.releaseDates) && Objects.equals(synopsis, other.synopsis)
				&& Objects.equals(title, other.title) && Objects.equals(url, other.url);
	}
}
