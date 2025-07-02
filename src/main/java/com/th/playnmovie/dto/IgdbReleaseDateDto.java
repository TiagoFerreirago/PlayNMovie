package com.th.playnmovie.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;


public class IgdbReleaseDateDto {
	
	   private Long date;
	 
	
	public IgdbReleaseDateDto(Long date) {
		this.date = date;
		
	}
	public IgdbReleaseDateDto() {}
	
	
	public LocalDate getLocalDate() {
		 if (date == null) {
		        return null; 
		    }
	        return Instant.ofEpochSecond(date)
	                      .atZone(ZoneId.systemDefault())
	                      .toLocalDate();
	    }
	public void setDate(Long date) {
		this.date = date;
	}
}
