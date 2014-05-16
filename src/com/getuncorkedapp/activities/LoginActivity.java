package com.getuncorkedapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.getuncorkedapp.utils.ParseKeys;
import com.getuncorkedapp.R;

public class LoginActivity extends Activity {
	
	private EditText	usernameField;
	private EditText	passwordField;
	private Button		registerButton;
	private Button		loginButton;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.register);
        loginButton = (Button) findViewById(R.id.login);
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent wineList = new Intent(LoginActivity.this, WineListActivity.class);
				startActivity(wineList);
			}
		});
        registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent second = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(second);
			}
		});
    }

}
