package com.th.playnmovie.util;

public class ConvertMovieData {

	public static Integer convertGenreNameToId(String gender) {
	    return switch (gender) {
	        case "Ação" -> 28;
	        case "Aventura" -> 12;
	        case "Animação" -> 16;
	        case "Comédia" -> 35;
	        case "Drama" -> 18;
	        case "Romance" -> 10749;
	        default -> 0;
	    
	    };
	}
	
	public static String convertGenreIdToName(Integer genreId) {
	    return switch (genreId) {
	        case 28 -> "Ação";
	        case 12 -> "Aventura";
	        case 16 -> "Animação";
	        case 35 -> "Comédia";
	        case 18 -> "Drama";
	        case 10749 -> "Romance";
	        default -> "Desconhecido";
	    
	    };
	}
}
