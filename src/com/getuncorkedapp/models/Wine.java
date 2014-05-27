package com.getuncorkedapp.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Wine")
public class Wine extends ParseObject {
	
	public Wine() {
		// leave empty
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
	
	public String getImageURL() {
		return getString("imageURL");
	}
	
	public void setImageURL(String image) {
		put("imageURL", image);
	}
	
	public ParseFile getImageFile() {
		return getParseFile("imageFile");
	}
	
	public void setImageFile(ParseFile file) {
		put("imageFile", file);
	}

}
