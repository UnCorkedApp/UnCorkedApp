/**
 * 
 */
package com.getuncorkedapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.getuncorkedapp.R;

/**
 * @author Alex
 *
 */
public class AddReviewActivity extends Activity {

	private String wineId;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_review);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		wineId = extras.getString(WineListActivity.WINE_ID_EXTRA);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		// TODO Auto-generated method stub

	}

}
