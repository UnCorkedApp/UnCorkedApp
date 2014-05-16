/**
 * 
 */
package com.getuncorkedapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.getuncorkedapp.R;
import com.getuncorkedapp.models.Wine;
import com.getuncorkedapp.utils.WebImageView;

/**
 * @author Alex
 *
 */
public class ViewWineActivity extends Activity {
	
	private Wine wine;
	
	private TextView wineName;
	private WebImageView wineIcon;
	private TextView wineryAndYear;
	private StringBuilder wineryYearSB = new StringBuilder();
	private ListView wineReviewList;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_wine);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		wine = (Wine) extras.get(WineListActivity.WINE_EXTRA);
		
		wineName = (TextView) findViewById(R.id.wineName);
		wineIcon = (WebImageView) findViewById(R.id.wineIcon);
		wineryAndYear = (TextView) findViewById(R.id.wineryAndYear);
		
		wineName.setText(wine.getName());
		//wineIcon.setImageUrl( wine.getImageFile().getUrl() );
		wineryYearSB.append( wine.getWinary() + "(");
		if ( wine.getYear() == 0) {
			wineryYearSB.append("N/A" + ")");
		} else {
			wineryYearSB.append( wine.getWinary() + ")" );
		}
		wineryAndYear.setText( wineryYearSB.toString() );
		
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		// TODO Auto-generated method stub

	}

}
