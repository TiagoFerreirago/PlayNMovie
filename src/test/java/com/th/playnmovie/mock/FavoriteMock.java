package com.th.playnmovie.mock;

import java.util.ArrayList;
import java.util.List;

import com.th.playnmovie.dto.FavoriteDto;
import com.th.playnmovie.model.Favorite;
import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.security.model.User;
import com.th.playnmovie.security.vo.UserVo;

public class FavoriteMock {

	public Favorite favoriteMock() {
		return favoriteMock(1L);
	}
	
	public FavoriteDto favoriteMockDto() {
		return favoriteMockDto(1L);
	}

	public FavoriteDto favoriteMockDto(Long i) {
		FavoriteDto dto = new FavoriteDto();
		dto.setId(i);
		dto.setItemId(2L);
		dto.setType(TypeEnum.FILME);
		dto.setUser(new UserVo());
		return dto;
	}

	public Favorite favoriteMock(Long i) {
		Favorite favorite = new Favorite();
		favorite.setId(i);
		favorite.setItemId(2L+i);
		favorite.setType(TypeEnum.GAME);
		favorite.setUser(new User());
		return favorite;
	}
	
	public List<Favorite> favoriteMockList() {
		List<Favorite> favorites = new ArrayList<Favorite>();
		for(int i = 0; i < 10; i++) {
			favorites.add(favoriteMock((long)i));
		}
		return favorites;
	}
	
	
	public List<FavoriteDto> favoriteMockDtoList() {
		List<FavoriteDto> favoritesDto = new ArrayList<FavoriteDto>();
		for(int i = 0; i < 10; i++) {
			favoritesDto.add(favoriteMockDto((long)i));
		}
		return favoritesDto;
	}
}
