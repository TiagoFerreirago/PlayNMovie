package com.th.playnmovie.dto;

import java.util.Objects;

import com.th.playnmovie.model.Favorite;
import com.th.playnmovie.security.model.User;

public class FavoriteDto {

	private Long id;
	
	private User user;
	
	private Long itemId;
	
	private String type;

	public FavoriteDto() {}
	
	public FavoriteDto(Favorite favorite) {
		this.id = favorite.getId();
		this.user = favorite.getUser();
		this.itemId = favorite.getItemId();
		this.type = favorite.getType();
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Long getItemId() {
		return itemId;
	}

	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, itemId, type, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FavoriteDto other = (FavoriteDto) obj;
		return Objects.equals(id, other.id) && Objects.equals(itemId, other.itemId) && Objects.equals(type, other.type)
				&& Objects.equals(user, other.user);
	}
	
}
