package com.getuncorkedapp.application;

import com.getuncorkedapp.models.User;
import com.getuncorkedapp.models.Wine;
import com.getuncorkedapp.utils.ParseKeys;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApp extends Application {

	private ParseObject user;

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, ParseKeys.APPID, ParseKeys.CLIENTKEY);
		user = new ParseObject("User");
		ParseObject.registerSubclass(User.class);
		ParseObject.registerSubclass( Wine.class );
		Parse.enableLocalDatastore(this);
		
	}

	public User getUser() {
		return (User) user;
	}

	public void setUser(ParseObject user) {
		this.user = user;
	}
	
	
	
}