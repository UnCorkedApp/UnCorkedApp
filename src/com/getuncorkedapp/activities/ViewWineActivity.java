/**
 * 
 */
package com.getuncorkedapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getuncorkedapp.R;
import com.getuncorkedapp.application.ParseApp;
import com.getuncorkedapp.models.Review;
import com.getuncorkedapp.models.Wine;
import com.getuncorkedapp.utils.WebImageView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

/**
 * @author Alex
 * @author Loran
 *
 */
public class ViewWineActivity extends Activity {
	
	private String wineId;
	private Wine wine;
	private ArrayList<Review> reviewList;
	private float reviewScore;
	
	private TextView wineName;
	private WebImageView wineIcon;
	private TextView wineryAndYear;
	private StringBuilder wineryYearSB = new StringBuilder();
	private ListView wineReviewList;
	private Button addReviewButton;
	private RatingBar wineRating;
	
	
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
		reviewList = new ArrayList<Review>();
		
		wineName = (TextView) findViewById(R.id.wineName);
		wineIcon = (WebImageView) findViewById(R.id.wineIcon);
		wineryAndYear = (TextView) findViewById(R.id.wineryAndYear);
		wineReviewList = (ListView) findViewById(R.id.reviewList);
		addReviewButton = (Button) findViewById(R.id.new_review_button);
		wineRating = (RatingBar) findViewById(R.id.view_wine_rating_bar);
		
		wineRating.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		wineRating.setFocusable(false);
		
		addReviewButton.setOnClickListener(new ReviewButtonListener());
		wineReviewList.setAdapter(new ReviewListAdpter(getApplicationContext(), reviewList));
		
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
//        query.fromLocalDatastore();
        query.getInBackground(id, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject win, ParseException error) {
				if (win != null) {
					wine = (Wine) win;
					Log.i("Wine", wine.getName() );
					fillInWine(wine);
					getReviews(wine);
				} else {
					Log.e("ParseException", error.getLocalizedMessage(), error );
				}
			}
        });
    }
	
	public void getReviews(Wine wine)
	{
		ParseRelation<Review> relation = wine.getRelation("reviews");
		ParseQuery<Review> query = relation.getQuery();
		query.addDescendingOrder("updatedAt");
		
		query.findInBackground(new FindCallback<Review>() {
			
			@Override
			public void done(List<Review> list, ParseException e) {
				int reviewSum = 0;
				for(Review r : list)
				{
					if(r.getUser().getUsername().equals(((ParseApp) getApplication()).getUser().get("username")))
					{
						list.remove(r);
						list.add(0, r);
					}
					reviewSum += r.getRating();
				}
				
				reviewScore = ((float) reviewSum) / list.size();
				wineRating.setRating(reviewScore);
				reviewList = new ArrayList<Review>(list);
				wineReviewList.postInvalidate();
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
	
	private class ReviewButtonListener implements android.view.View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class ReviewListAdpter extends ArrayAdapter<Review>
	{
		Context context;
		ArrayList<Review> reviews;
		
		public ReviewListAdpter(Context context, ArrayList<Review> reviews) {
			super(context, R.layout.row_wine_review, reviews);
			this.context = context;
			this.reviews = reviews;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if(convertView == null)
			{
				LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflator.inflate(R.layout.row_wine_review, parent, false);
			}
			
			Review review = reviews.get(position);
			TextView userView = (TextView) convertView.findViewById(R.id.review_user);
			RatingBar ratingView = (RatingBar) convertView.findViewById(R.id.wine_review_rating);
			TextView reviewView = (TextView) convertView.findViewById(R.id.wine_review);
			
			userView.setText(review.getUser().getUsername());
			ratingView.setRating(review.getRating());
			reviewView.setText(review.getComment());
			
			return convertView;
		}
	}

}
