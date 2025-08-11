package com.th.playnmovie.mapper;

import com.th.playnmovie.dto.FavoriteDto;
import com.th.playnmovie.model.Favorite;

public class FavoriteMapper {

	
	public static Favorite fromDto(FavoriteDto dto) {
		Favorite favorite = new Favorite();
		favorite.setId(dto.getId());
		favorite.setItemId(dto.getItemId());
		favorite.setType(dto.getType());
		favorite.setUser(UserMapper.tokenResponse(dto.getUser()));
		return favorite;
	}
	
	public static FavoriteDto toDto(Favorite favorite) {
		FavoriteDto dto = new FavoriteDto();
		dto.setId(favorite.getId());
		dto.setItemId(favorite.getItemId());
		dto.setType(favorite.getType());
		dto.setUser(UserMapper.tokenResponseVo(favorite.getUser()));
		return dto;
	}
}
