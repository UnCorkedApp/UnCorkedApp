package com.getuncorkedapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.getuncorkedapp.R;

public class RegisterActivity extends Activity {

	private EditText	usernameField;
	private EditText	passwordField;
	private Button		registerButton;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.register);
        
        registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				usernameField.setText("You clicked login");
				
			}
		});
		
	}
}
