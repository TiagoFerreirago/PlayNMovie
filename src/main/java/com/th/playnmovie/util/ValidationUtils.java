package com.th.playnmovie.util;

import java.util.List;

public class ValidationUtils {

	public static boolean isBlank(String str) {
	        return str == null || str.trim().isEmpty();
	 }

	public static boolean isEmpty(List<?> list) {
	        return list == null || list.isEmpty();
	 }
}
