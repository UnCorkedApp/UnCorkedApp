package com.getuncorkedapp.models;

import java.io.Serializable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Review")
public class Review extends ParseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Review() {
		// leave empty
	}
	
	public User getUser() {
		return (User) getParseObject("user");
	}
	
	public void setUser(User user) {
		put("user", user);
	}
	
	public Wine getWine() {
		return (Wine) getParseObject("wine");
	}
	
	public void setWine(Wine wine) {
		put("wine", wine);
	}

	public int getRating() {
		return getInt("rating");
	}

	public void setRating(int rating) {
		put("rating", rating);
	}
	
	public String getComment() {
		return getString("comment");
	}

	public void setComment(String comment) {
		put("comment", comment);
	}

}
