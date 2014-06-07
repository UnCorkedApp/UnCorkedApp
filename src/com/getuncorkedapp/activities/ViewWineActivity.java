/**
 * 
 */
package com.getuncorkedapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getuncorkedapp.R;
import com.getuncorkedapp.application.ParseApp;
import com.getuncorkedapp.models.Review;
import com.getuncorkedapp.models.User;
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
public class ViewWineActivity extends Activity
{
	public static final String UPDATE_REVIEWS_ACTION = "com.getuncorkedapp.ViewWineActivity.UPDATE_REVIEWS";

	private String wineId;
	private Wine wine;
	private ArrayList<Review> reviewList;
	private float reviewScore;
	private ParseApp app;

	private TextView wineName;
	private WebImageView wineIcon;
	private TextView wineryAndYear;
	private StringBuilder wineryYearSB = new StringBuilder();
	private ListView wineReviewList;
	private Button addReviewButton;
	private RatingBar wineRating;
	private ReviewListAdpter reviewAdapter;
	private ViewWineBroadcastReceiver localBroadcastReceiver;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_wine);
		app = (ParseApp) getApplication();

		LocalBroadcastManager bManager = LocalBroadcastManager
			.getInstance(this);
		localBroadcastReceiver = new ViewWineBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UPDATE_REVIEWS_ACTION);
		bManager.registerReceiver(localBroadcastReceiver, intentFilter);

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
		reviewAdapter = new ReviewListAdpter(getApplicationContext(),
			reviewList);

		wineRating.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
		wineRating.setFocusable(false);

		addReviewButton.setOnClickListener(new ReviewButtonListener());
		wineReviewList.setAdapter(reviewAdapter);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop()
	{
		super.onStop();
		// TODO Auto-generated method stub

	}

	public void findWine(String id)
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Wine");
		// query.fromLocalDatastore();
		query.getInBackground(id, new GetCallback<ParseObject>()
		{

			@Override
			public void done(ParseObject win, ParseException error)
			{
				if (win != null)
				{
					wine = (Wine) win;
					Log.i("Wine", wine.getName());
					fillInWine(wine);
					// scaleImage();
					getReviews(wine);
				}
				else
				{
					Log.e("ParseException", error.getLocalizedMessage(), error);
				}
			}
		});
	}

	public void getReviews(Wine wine)
	{
		Log.i("Get Reviews", "Called");
		ParseRelation<Review> relation = wine.getRelation("reviews");
		ParseQuery<Review> reviewQuery = relation.getQuery();
		reviewQuery.addDescendingOrder("updatedAt");

		reviewQuery.findInBackground(new FindCallback<Review>()
		{

			@Override
			public void done(List<Review> list, ParseException e)
			{
				if (e == null)
				{
					int reviewSum = 0;
					for (final Review r : list)
					{
						Log.i("Add Review to Array Adapter", "Review "
							+ r.toString());

						r.getUser().fetchInBackground(new GetCallback<User>()
						{

							@Override
							public void done(User arg0, ParseException arg1)
							{
								if (arg0.getUsername() != null
									&& arg0.getObjectId().equals(
										(app.getUser().getObjectId())))
								{
									reviewAdapter.insert(r, 0);
								}
								else
								{
									reviewAdapter.add(r);
								}
							}

						});

						reviewSum += r.getRating();
					}
					reviewScore = ((float) reviewSum) / list.size();
					Log.i("Set Rating", reviewScore + "");
					wineRating.setRating(reviewScore);
				}
				else
				{
					Log.e("Get Reviews Excep", e.getLocalizedMessage(), e);
				}
			}
		});
	}

	public void fillInWine(Wine wine)
	{
		wineName.setText(wine.getName());
		wineIcon.setImageUrl(wine.getImageFile().getUrl());
		wineryYearSB.append(wine.getWinary() + "(");
		if (wine.getYear() == 0)
		{
			wineryYearSB.append("N/A" + ")");
		}
		else
		{
			wineryYearSB.append(wine.getYear() + ")");
		}
		wineryAndYear.setText(wineryYearSB.toString());

	}

	private class ReviewButtonListener implements
		android.view.View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent startAddReviewIntent = new Intent(getApplicationContext(),
				AddReviewActivity.class);
			Bundle extras = new Bundle();
			extras.putString(WineListActivity.WINE_ID_EXTRA, wineId);
			startAddReviewIntent.putExtras(extras);
			startActivity(startAddReviewIntent);
		}
	}

	//Currently untested, so unused
	private void scaleImage()
	{
		// Get the ImageView and its bitmap
		WebImageView view = (WebImageView) findViewById(R.id.wineIcon);
		Drawable drawing = view.getDrawable();
		if (drawing == null)
		{
			return; // Checking for null & return, as suggested in comments
		}
		Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

		// Get current dimensions AND the desired bounding box
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int bounding = dpToPx(250);
		Log.i("Test", "original width = " + Integer.toString(width));
		Log.i("Test", "original height = " + Integer.toString(height));
		Log.i("Test", "bounding = " + Integer.toString(bounding));

		// Determine how much to scale: the dimension requiring less scaling is
		// closer to the its side. This way the image always stays inside your
		// bounding box AND either x/y axis touches it.
		float xScale = ((float) bounding) / width;
		float yScale = ((float) bounding) / height;
		float scale = xScale;
		Log.i("Test", "xScale = " + Float.toString(xScale));
		Log.i("Test", "yScale = " + Float.toString(yScale));
		Log.i("Test", "scale = " + Float.toString(scale));

		// Create a matrix for the scaling and add the scaling data
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		// Create a new bitmap and convert it to a format understood by the
		// ImageView
		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
			matrix, true);
		width = scaledBitmap.getWidth(); // re-use
		height = scaledBitmap.getHeight(); // re-use
		BitmapDrawable result = new BitmapDrawable(getResources(), scaledBitmap);
		Log.i("Test", "scaled width = " + Integer.toString(width));
		Log.i("Test", "scaled height = " + Integer.toString(height));

		// Apply the scaled bitmap
		view.setImageDrawable(result);

		// Now change ImageView's dimensions to match the scaled image
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
			.getLayoutParams();
		params.width = width;
		params.height = height;
		view.setLayoutParams(params);

		Log.i("Test", "done");
	}

	private int dpToPx(int dp)
	{
		float density = getApplicationContext().getResources()
			.getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	private class ReviewListAdpter extends ArrayAdapter<Review>
	{
		Context context;
		ArrayList<Review> reviews;

		public ReviewListAdpter(Context context, ArrayList<Review> reviews)
		{
			super(context, R.layout.row_wine_review, reviews);
			this.context = context;
			this.reviews = reviews;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				LayoutInflater inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflator.inflate(R.layout.row_wine_review,
					parent, false);
			}

			Review review = reviews.get(position);
			TextView userView = (TextView) convertView
				.findViewById(R.id.review_user);

			RatingBar ratingView = (RatingBar) convertView
				.findViewById(R.id.wine_review_rating);
			TextView reviewView = (TextView) convertView
				.findViewById(R.id.wine_review);

			if (review.getUser().getUsername() != null)
			{
				userView.setText(review.getUser().getUsername());
			}
			else
			{
				userView.setText(R.string.review_user_default);
			}

			ratingView.setRating(review.getRating());
			ratingView.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					return true;
				}
			});
			ratingView.setFocusable(false);
			reviewView.setText(review.getComment());

			return convertView;
		}
	}

	private class ViewWineBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction() == UPDATE_REVIEWS_ACTION)
			{
				if (intent.getExtras()
					.getString(WineListActivity.WINE_ID_EXTRA).equals(
						wine.getObjectId()))
				{
					reviewAdapter.clear();
					getReviews(wine);
				}
			}
		}

	}

}
