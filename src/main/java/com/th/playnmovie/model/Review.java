package com.th.playnmovie.model;

import java.util.Objects;

import com.th.playnmovie.security.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User user;
	
	private Long itemId;
	
	private String type;
	
	private String comment;
	
	private Integer notice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getNotice() {
		return notice;
	}

	public void setNotice(Integer notice) {
		this.notice = notice;
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
		Review other = (Review) obj;
		return Objects.equals(comment, other.comment) && Objects.equals(id, other.id)
				&& Objects.equals(itemId, other.itemId) && Objects.equals(notice, other.notice)
				&& Objects.equals(type, other.type) && Objects.equals(user, other.user);
	}
	
	
}
