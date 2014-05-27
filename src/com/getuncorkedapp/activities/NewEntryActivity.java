package com.getuncorkedapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.getuncorkedapp.R;

public class NewEntryActivity extends Activity {

	private EditText	wineName;
	private EditText	winery;
	private EditText	year;
	private EditText	description;
	private Button		add;
	private Button		cancel;
	private RatingBar ratingBar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        wineName = (EditText) findViewById(R.id.wine_name);
        winery = (EditText) findViewById(R.id.winery);
        year = (EditText) findViewById(R.id.year);
        description = (EditText) findViewById(R.id.description);
        add = (Button) findViewById(R.id.add_new_entry);
        cancel = (Button) findViewById(R.id.cancel);
        
        
        add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent second = new Intent(NewEntryActivity.this, LoginActivity.class);
				startActivity(second);
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent second = new Intent(NewEntryActivity.this, LoginActivity.class);
				startActivity(second);
			}
		});
	}
	
	 public void addListenerOnRatingBar() {
		 
			ratingBar = (RatingBar) findViewById(R.id.wine_review_rating);
			//txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
		 
			//if rating value is changed,
			//display the current rating value in the result (textview) automatically
			ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
				public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
		 
					//Parse.add(String.valueOf(rating));
					//txtRatingValue.setText(String.valueOf(rating));
		 
				}
			});
		  }
	
}
