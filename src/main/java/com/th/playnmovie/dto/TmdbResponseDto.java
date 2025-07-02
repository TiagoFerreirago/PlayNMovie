package com.th.playnmovie.dto;

import java.util.List;
import java.util.Objects;

public class TmdbResponseDto {

	private List<MovieDto> results;
	private int total_pages;

	public List<MovieDto> getResults() {
		return results;
	}

	public void setResults(List<MovieDto> results) {
		this.results = results;
	}

	@Override
	public int hashCode() {
		return Objects.hash(results);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TmdbResponseDto other = (TmdbResponseDto) obj;
		return Objects.equals(results, other.results);
	}

	public int getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}
	
	
}
