/**
 * 
 */
package com.getuncorkedapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.getuncorkedapp.R;
import com.getuncorkedapp.models.Wine;
import com.getuncorkedapp.utils.WebImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

/**
 * @author Alex
 *
 */
public class ViewWineActivity extends Activity {
	
	private String wineId;
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
		wineId = extras.getString(WineListActivity.WINE_ID_EXTRA);
		Log.i("ViewWineActivity", wineId);
		findWine(wineId);
		
		wineName = (TextView) findViewById(R.id.wineName);
		wineIcon = (WebImageView) findViewById(R.id.wineIcon);
		wineryAndYear = (TextView) findViewById(R.id.wineryAndYear);
		
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
	
	public void findWine(String id) {
        ParseQuery<Wine> query = ParseQuery.getQuery(Wine.class);
        query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
        query.getInBackground(id, new GetCallback<Wine>() {

			@Override
			public void done(Wine wine, ParseException error) {
				if (wine != null) {
					wine = (Wine) wine;
					Log.i("Wine", wine.getName() );
					fillInWine(wine);
				} else {
					Log.e("ParseException", error.getLocalizedMessage() );
				}
			}
        });
    }
	
	public void fillInWine(Wine wine) {
		wineName.setText(wine.getName());
		wineIcon.setImageUrl( wine.getImageFile().getUrl() );
		wineryYearSB.append( wine.getWinary() + "(");
		if ( wine.getYear() == 0) {
			wineryYearSB.append("N/A" + ")");
		} else {
			wineryYearSB.append( wine.getWinary() + ")" );
		}
		wineryAndYear.setText( wineryYearSB.toString() );
	}

}
