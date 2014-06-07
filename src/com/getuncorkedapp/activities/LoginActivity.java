package com.getuncorkedapp.activities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.getuncorkedapp.R;
import com.getuncorkedapp.application.ParseApp;
import com.getuncorkedapp.models.User;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LoginActivity extends Activity {

	public static final String USERINFO = "UserInfoFile";
	
	private EditText usernameField;
	private EditText passwordField;
	private CheckBox rememberMeBox;
	private Button registerButton;
	private Button loginButton;
	private Context loginContext;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginContext = this;
		
		SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		String uname = userInfo.getString("username", "");
		String pword = userInfo.getString("password", "");
		
		Log.i("LoginAct", uname);
		Log.i("LoginAct", pword);
		
		if (!uname.isEmpty() && !pword.isEmpty() ) {
			Log.i("LoginAct", "attempting to login");
			if ( checkAccount(uname, pword, false) ) {
				Toast.makeText(loginContext,
						"You have Login from Saved Info",
						Toast.LENGTH_SHORT).show();
				Intent wineList = new Intent(LoginActivity.this,
						WineListActivity.class);
				startActivity(wineList);
				finish();
			}
		}

		usernameField = (EditText) findViewById(R.id.username);
		passwordField = (EditText) findViewById(R.id.password);
		rememberMeBox   = (CheckBox) findViewById(R.id.remember);
		registerButton = (Button) findViewById(R.id.register);
		loginButton = (Button) findViewById(R.id.login);
		

		ParseAnalytics.trackAppOpened(getIntent());
		
		

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String username = usernameField.getText().toString();
				String password = "";
				try {
					password = hashPassword(passwordField.getText().toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (!username.isEmpty()) {
					if ( checkAccount( username, password, rememberMeBox.isChecked() ) ) {
						Toast.makeText(loginContext,
								"You have Login Successfully",
								Toast.LENGTH_SHORT).show();
						Intent wineList = new Intent(LoginActivity.this,
								WineListActivity.class);
						startActivity(wineList);
						finish();
					} else {
						usernameField.setText("");
						passwordField.setText("");
						passwordField.clearFocus();
						Toast.makeText(loginContext,
								"Username or Password is wrong.",
								Toast.LENGTH_SHORT).show();
					}
				} else
					Toast.makeText(loginContext,
							"You must input your login information.",
							Toast.LENGTH_SHORT).show();
			}
		});
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));
			}
		});
	}

	private boolean checkAccount(final String username, final String password, boolean rememberMe) {
		Boolean check = false;
		List<User> data = null;
		ParseObject user = null;
		ParseQuery<User> userQuery = ParseQuery.getQuery("User");
		try {
			data = userQuery.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (User u : data) {
			if (password.equalsIgnoreCase(u.getPassword())
					&& username.equalsIgnoreCase(u.getUsername())) {
				user = u;
			}
		}
		
		if (!(user == null)) {
			
			if (((String) user.get("password")).equalsIgnoreCase(password)
					&& ((String) user.get("username"))
							.equalsIgnoreCase(username)) {
				Log.e("CheckAccount","PASS");
				Log.e("CheckAccount",user.get("username")+"");
				check = true;
				ParseApp app = (ParseApp) getApplication();
				app.setUser(user);
				if (rememberMe) {
					persistUser( (User) user);
				}
			}
		}
		return check;
	}

	private void persistUser(User user) {
		SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor edit = userInfo.edit();
		edit.clear();
		edit.putString( "username", user.getUsername() );
		edit.putString( "password", user.getPassword() );
		edit.commit();
		Toast.makeText(loginContext, "Login details are saved..", Toast.LENGTH_SHORT).show();
	}

	public String hashPassword(String pass) throws UnsupportedEncodingException {
		byte[] bytesOfMessage = pass.getBytes("UTF-8");

		MessageDigest md;
		String hashed = "";
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] hashedpass = md.digest(bytesOfMessage);

			hashed = new String(hashedpass, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashed;
	}

}
