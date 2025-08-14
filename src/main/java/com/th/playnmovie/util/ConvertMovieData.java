package com.th.playnmovie.util;

public class ConvertMovieData {

	public static Integer convertGenreNameToId(String genre) {
	    return switch (genre) {
	        case "Action" -> 28;
	        case "Adventure" -> 12;
	        case "Animation" -> 16;
	        case "Comedy" -> 35;
	        case "Drama" -> 18;
	        case "Romance" -> 10749;
	        default -> 0;
	    };
	}

	public static String convertGenreIdToName(Integer genreId) {
	    return switch (genreId) {
	        case 28 -> "Action";
	        case 12 -> "Adventure";
	        case 16 -> "Animation";
	        case 35 -> "Comedy";
	        case 18 -> "Drama";
	        case 10749 -> "Romance";
	        default -> "Unknown";
	    };
	}
}
