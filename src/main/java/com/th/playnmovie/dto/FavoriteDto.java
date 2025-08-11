package com.th.playnmovie.dto;

import java.util.Objects;

import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.security.vo.UserVo;

public class FavoriteDto {

	private Long id;
	
	private UserVo user;
	
	private Long itemId;
	
	private TypeEnum type;

	public FavoriteDto() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
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
		return Objects.equals(id, other.id) && Objects.equals(itemId, other.itemId) && type == other.type
				&& Objects.equals(user, other.user);
	}

	
}
