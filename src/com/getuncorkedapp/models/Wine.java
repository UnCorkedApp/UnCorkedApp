package com.getuncorkedapp.models;

public class Wine {
	private int id;
	private String name;
	private int year;
	private String type;
	private String winery;
	private String imageId;

	public Wine(int id, String name, int year, String type, String winery) {
		super();
		this.id = id;
		this.name = name;
		this.year = year;
		this.type = type;
		this.winery = winery;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWinery() {
		return winery;
	}

	public void setWinery(String winery) {
		this.winery = winery;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

}
