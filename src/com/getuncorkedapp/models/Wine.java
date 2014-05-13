package com.getuncorkedapp.models;

import java.io.Serializable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Wine")
public class Wine extends ParseObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Wine() {
		// leave empty
	}
	
	public String getId() {
		return getString("objectId");
	}
	
	public String getName() {
		return getString("name");
	}
	
	public void setName(String name) {
		put("name", name);
	}
	
	public Integer getYear() {
		return getInt("year");
	}
	
	public void setYear(Integer year) {
		put("year", year);
	}
	
	public String getWinary() {
		return getString("winary");
	}
	
	public void setWinary(String winary) {
		put("winary", winary);
	}
	
	public String getType() {
		return getString("type");
	}
	
	public void setType(String type) {
		put("type", type);
	}
	
	public String getImage() {
		return getString("image");
	}
	
	public void setImage(String image) {
		put("image", image);
	}
}
