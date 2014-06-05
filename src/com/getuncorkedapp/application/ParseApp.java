package com.getuncorkedapp.application;

import com.getuncorkedapp.models.Review;
import com.getuncorkedapp.models.User;
import com.getuncorkedapp.models.Wine;
import com.getuncorkedapp.utils.ParseKeys;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApp extends Application {

	private ParseObject user;
	private ParseObject wine;
	private ParseObject reviewParse;

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, ParseKeys.APPID, ParseKeys.CLIENTKEY);
		user = new ParseObject("User");
		wine = new ParseObject("Wine");
		reviewParse = new ParseObject("Review");
		ParseObject.registerSubclass(User.class);
		ParseObject.registerSubclass( Wine.class );
		ParseObject.registerSubclass( Review.class );
		Parse.enableLocalDatastore(this);
		
		
	}

	public ParseObject getUser() {
		user = ParseObject.create("User");
		return user;
	}

	public void setUser(ParseObject user) {	
		this.user = user;
	}
	
	public ParseObject getWine() {
		wine = ParseObject.create("Wine");
		return wine;
	}

	public void setWine(ParseObject wine) {
		this.wine = wine;
	}
	
	public ParseObject getReviewParse() {
		reviewParse = ParseObject.create("Review");
		return reviewParse;
	}

	public void setReviewParse(ParseObject reviewParse) {
		this.reviewParse = reviewParse;
	}
	
}