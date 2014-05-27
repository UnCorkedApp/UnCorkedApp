package com.getuncorkedapp.models;

import java.io.Serializable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User() {
		// leave empty
	}
	
	public String getId() {
		return getString("objectId");
	}

	public String getUsername() {
		return getString("username");
	}

	public void setUsername(String username) {
		put("username", username);
	}

	public String getPassword() {
		return getString("password");
	}

	public void setPassword(String password) {
		put("password", password);
	}

	public String getEmail() {
		return getString("email");
	}

	public void setEmail(String email) {
		put("email", email);
	}
	
	public Boolean isEmailVerified() {
		return getBoolean("emailVerified");
	}
	
	public void setEmailVerified(Boolean bool) {
		put("emailVerified", bool);
	}

}
