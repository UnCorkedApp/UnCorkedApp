/**
 * 
 */
package com.getuncorkedapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.getuncorkedapp.R;
import com.getuncorkedapp.models.Review;
import com.getuncorkedapp.models.Wine;
import com.getuncorkedapp.utils.WebImageView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseQuery.CachePolicy;

/**
 * @author Alex
 * @author Loran
 *
 */
public class ViewWineActivity extends Activity {
	
	private String wineId;
	private Wine wine;
	private ArrayList<Review> reviewList;
	
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Wine");
        query.fromLocalDatastore();
        query.getInBackground(id, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject win, ParseException error) {
				if (wine != null) {
					wine = (Wine) win;
					Log.i("Wine", wine.getName() );
					fillInWine(wine);
				} else {
					Log.e("ParseException", error.getLocalizedMessage() );
				}
			}
        });
    }
	
	public void getReviews(Wine wine)
	{
		ParseRelation<Review> relation = wine.getRelation("reviews");
		ParseQuery<Review> queryUser = relation.getQuery();
		queryUser.addAscendingOrder("createdAt");
		ParseQuery<Review> queryNotUser = relation.getQuery();
		
		
		
//		query.findInBackground(new FindCallback<Review>() {
//
//			@Override
//			public void done(List<Review> list, ParseException e) {
//				if(e == null)
//				{
//					list.add(0, list.re);
//				}
//				else
//				{
//					Log.w("Review Query", e.getMessage());
//				}
//			}
//			
//		});
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
