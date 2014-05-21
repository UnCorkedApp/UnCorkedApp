package com.getuncorkedapp.activities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getuncorkedapp.R;
import com.getuncorkedapp.models.User;
import com.getuncorkedapp.utils.ParseKeys;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RegisterActivity extends Activity {

	private EditText usernameField;
	private EditText passwordField;
	private EditText emailField;
	private Button registerButton;
	Context RegisterContext;
	ParseObject user;

	protected String mailCheck = "";
	private String userCheck = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		RegisterContext = this;

		Parse.initialize(this, ParseKeys.APPID, ParseKeys.CLIENTKEY);
		ParseAnalytics.trackAppOpened(getIntent());
		user = new ParseObject("User");
		ParseObject.registerSubclass(User.class);
		// ParseObject.create(User.class);


		usernameField = (EditText) findViewById(R.id.username);
		passwordField = (EditText) findViewById(R.id.password);
		registerButton = (Button) findViewById(R.id.register);
		emailField = (EditText) findViewById(R.id.email);

		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Register", "Register buttom has been clicked.");
				CharSequence email = (CharSequence) emailField.getText();
				String username = usernameField.getText().toString();
				String pass = passwordField.getText().toString();

				if (passwordField.getText().toString().isEmpty()
						|| username.isEmpty()) {
					Toast.makeText(RegisterContext, "Fill in the empty field.",
							Toast.LENGTH_SHORT).show();
				} else {
					if (!isValidEmail(email)) {
						Toast.makeText(RegisterContext,
								"Input a correct email.", Toast.LENGTH_SHORT)
								.show();
					} else if (checkAccount(username, email.toString())) {
						try {
							pass = hashPassword(pass);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						Log.e("email", email + "");
						Log.e("Username", username);
						Log.e("Password", pass);

						user.put("username", username);
						user.put("email", email.toString());
						user.put("password", pass);
						user.saveInBackground();

						AlertDialog.Builder confirmation = new AlertDialog.Builder(
								RegisterContext);
						confirmation
								.setMessage("You have succesfully Register.");
						confirmation.setCancelable(false);
						confirmation.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

						AlertDialog msgpop = confirmation.create();
						msgpop.show();
					}
				}
			}
		});
	}

	public final boolean isValidEmail(CharSequence s) {
		if (TextUtils.isEmpty(s)) {
			Toast.makeText(this, "Email Field must be filled.",
					Toast.LENGTH_SHORT).show();
			// Log.e("EmailCheck", "Empty");
			return false;
		} else {
			// Log.e("Valid email called", "Checking...");
			return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
		}
	}

	public String hashPassword(String pass) throws UnsupportedEncodingException {
		byte[] bytesOfMessage = pass.getBytes("UTF-8");

		MessageDigest md;
		String hashed = "";
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] hashedpass = md.digest(bytesOfMessage);
			// for(byte i : hashedpass)
			// Log.e("BYTES", i+"");
			hashed = new String(hashedpass, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// for(byte a: hashed.getBytes())
		// Log.e("STRING", a+"");

		// Log.e("Password", hashed);
		return hashed;
	}

	public boolean checkAccount(final String username, final String email) {
		Boolean check = true;
		List<User> data = null;
		ParseQuery<User> userQuery = ParseQuery.getQuery("User");
		try {
			data = userQuery.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (User u : data) {
			if (email.equalsIgnoreCase(u.getEmail())
					&& username.equalsIgnoreCase(u.getUsername())) {
				mailCheck = u.getEmail();
				userCheck = u.getUsername();
			}
		}

		Log.e("CheckingMail", mailCheck);
		Log.e("CheckingUsername", userCheck);
		if (mailCheck.equalsIgnoreCase(email)) {
			Toast.makeText(this, "Email already taken.", Toast.LENGTH_SHORT)
					.show();
			check = false;
		} else if (userCheck.equalsIgnoreCase(username)) {
			Toast.makeText(this, "Username already taken.", Toast.LENGTH_SHORT)
					.show();
			check = false;
		}

		return check;
	}

	// public boolean passwordCheck(String pass) {
	// boolean upper = false;
	// boolean lower = false;
	// boolean number = false;
	// while (true) {
	// if (pass.length() < 7) {
	// Toast.makeText(this,
	// "must have at least 7 character.",
	// Toast.LENGTH_SHORT).show();
	//
	// } else {
	// for (char c : pass.toCharArray()) {
	// if (Character.isUpperCase(c)) {
	// upper = true;
	// } else if (Character.isLowerCase(c)) {
	// lower = true;
	// } else if (Character.isDigit(c)) {
	// number = true;
	// }
	// }
	// if (!upper) {
	// Toast.makeText(this,
	// "must contain at least one uppercase character",
	// Toast.LENGTH_SHORT).show();
	// } else if (!lower) {
	// Toast.makeText(this,
	// "must contain at least one lowercase character",
	// Toast.LENGTH_SHORT).show();
	// } else if (!number) {
	// Toast.makeText(this, "must contain at least one number",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// break;
	// }
	// }
	// }
	// return upper && lower && number;
	// }

}
