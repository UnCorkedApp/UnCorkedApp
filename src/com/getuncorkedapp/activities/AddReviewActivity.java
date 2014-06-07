/**
 * 
 */
package com.getuncorkedapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getuncorkedapp.R;
import com.getuncorkedapp.application.ParseApp;
import com.getuncorkedapp.models.Review;
import com.getuncorkedapp.models.Wine;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

/**
 * @author Alex
 * 
 */
public class AddReviewActivity extends Activity
{

	private String wineId;
	private Wine wine;

	private TextView wineName;
	private RatingBar newRating;
	private EditText editComment;
	private Button addReview;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_review);

		wineName = (TextView) findViewById(R.id.new_review_wine_title);
		newRating = (RatingBar) findViewById(R.id.new_review_rating_bar);
		editComment = (EditText) findViewById(R.id.new_review_comment);
		addReview = (Button) findViewById(R.id.add_review_button);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		wineId = extras.getString(WineListActivity.WINE_ID_EXTRA);

		ParseQuery<Wine> wineQuery = ParseQuery.getQuery("Wine");
		wineQuery.getInBackground(wineId, new GetCallback<Wine>()
		{

			@Override
			public void done(Wine arg0, ParseException arg1)
			{
				wine = arg0;
				wineName.setText(wine.getName());
				addReview.setOnClickListener(new AddReviewClickListener());
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause()
	{
		super.onPause();
		// TODO Auto-generated method stub

	}

	private class AddReviewClickListener implements
		android.view.View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			final Context context = getApplicationContext();
			if (editComment.getText().toString().isEmpty())
			{
				Toast.makeText(context, "Pleace add something in the comment.",
					Toast.LENGTH_LONG).show();
			}
			else if (newRating.getRating() == 0.0)
			{
				Toast.makeText(context, "Please make a review greater than 0.",
					Toast.LENGTH_LONG).show();
			}
			else
			{
				ParseApp app = (ParseApp) getApplication();
				final Review newReview = (Review) ParseObject.create("Review");
				newReview.put("wine", wine);
				newReview.put("user", app.getUser());
				newReview.put("comment", editComment.getText().toString());
				newReview.put("rating", newRating.getRating());

				newReview.saveInBackground(new SaveCallback()
				{
					@Override
					public void done(ParseException arg0)
					{
						if (arg0 == null)
						{
							Toast.makeText(context, "Review Saved",
								Toast.LENGTH_LONG).show();

							ParseRelation<Review> wineReviews = wine
								.getRelation("reviews");
							wineReviews.add((Review) newReview);

							wine.saveInBackground(new SaveCallback()
							{
								@Override
								public void done(ParseException arg0)
								{
									if (arg0 == null)
									{
										Intent updateReviewsIntent = new Intent(
											ViewWineActivity.UPDATE_REVIEWS_ACTION);
										updateReviewsIntent.putExtra(
											WineListActivity.WINE_ID_EXTRA,
											wine.getObjectId());
										LocalBroadcastManager.getInstance(
											AddReviewActivity.this)
											.sendBroadcast(updateReviewsIntent);

										finish();
									}
									else
									{
										Log.e("Save Wine in Review", arg0
											.getLocalizedMessage(), arg0);
									}
								}
							});
						}
						else
						{
							Log.e("Save Review", arg0.getLocalizedMessage(),
								arg0);
						}
					}
				});
			}
		}
	}

}
