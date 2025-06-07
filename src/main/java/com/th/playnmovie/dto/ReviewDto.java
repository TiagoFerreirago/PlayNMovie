package com.th.playnmovie.dto;

import java.util.Objects;

import com.th.playnmovie.model.Review;
import com.th.playnmovie.security.model.User;

public class ReviewDto {

	private Long id;
	private User user;
	private Long itemId;
	private String type;
	private String comment;
	private Integer notice;
	
	public ReviewDto() {}

	public ReviewDto(Review review) {
		this.id = review.getId();
		this.user = review.getUser();
		this.itemId = review.getItemId();
		this.type = review.getType();
		this.comment = review.getComment();
		this.notice = review.getNotice();
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

	public String getComment() {
		return comment;
	}

	public Integer getNotice() {
		return notice;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comment, id, itemId, notice, type, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReviewDto other = (ReviewDto) obj;
		return Objects.equals(comment, other.comment) && Objects.equals(id, other.id)
				&& Objects.equals(itemId, other.itemId) && Objects.equals(notice, other.notice)
				&& Objects.equals(type, other.type) && Objects.equals(user, other.user);
	}
	
	
}
