package com.getuncorkedapp.activities;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.getuncorkedapp.R;
import com.getuncorkedapp.application.ParseApp;
import com.getuncorkedapp.models.Review;
import com.getuncorkedapp.utils.BitmapProcessor;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;

public class NewEntryActivity extends Activity {

	private EditText wineName;
	private EditText winery;
	private EditText year;
	private EditText review;
	private Button add;
	private Button cancel;
	private RatingBar ratingBar;
	private ParseObject wine;
	private ParseObject reviewParse;
	private ParseObject user;
	// Image variables
	private ImageView imageView;
	private Button buttonNewPic;
	private Button buttonImage;
	private ParseFile imageFile;

	private Bitmap image;

	private static final int IMAGE_PICK = 1;
	private static final int IMAGE_CAPTURE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ParseApp app = (ParseApp) getApplication();
		wine = app.getWine();
		reviewParse = app.getReviewParse();
		user = app.getUser();
		setContentView(R.layout.activity_new_entry);

		wineName = (EditText) findViewById(R.id.wine_name);
		winery = (EditText) findViewById(R.id.winery);
		year = (EditText) findViewById(R.id.year);
		review = (EditText) findViewById(R.id.review);
		add = (Button) findViewById(R.id.add_new_entry);
		cancel = (Button) findViewById(R.id.cancel);

		imageView = (ImageView) findViewById(R.id.imageView);
		buttonNewPic = (Button) findViewById(R.id.button_camera);
		buttonImage = (Button) findViewById(R.id.button_from_phone);

		buttonImage.setOnClickListener(new ImagePickListener());
		buttonNewPic.setOnClickListener(new TakePictureListener());

		addListenerOnRatingBar();

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				imageFile = addImage();
				imageFile.saveInBackground(new SaveImageCallback());

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent second = new Intent(NewEntryActivity.this,
						WineListActivity.class);
				startActivity(second);
				finish();
			}
		});
	}

	public ParseObject addWineRelations() {
		ParseRelation<Review> relation = wine.getRelation("reviews");
		relation.add((Review) reviewParse);
		return reviewParse;
	}

	public ParseObject addReviewRelations() {
		reviewParse.put("wine", wine);
		reviewParse.put("user", user);
		return reviewParse;
	}

	public ParseObject addToReview() {
		String reviewTxt = review.getText().toString();
		String rating = String.valueOf(ratingBar.getRating());
		Double ratingD = Double.parseDouble(rating);

		reviewParse.put("comment", reviewTxt);
		reviewParse.put("rating", ratingD);
		return reviewParse;
	}

	public ParseFile addImage() {
		String wineNameTxt = wineName.getText().toString();
		// Locate the image
		Bitmap bitmap = image;
		// Convert it to byte
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// Compress image to lower quality scale 1 - 100
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] image = stream.toByteArray();

		// Create the ParseFile
		ParseFile file = new ParseFile(wineNameTxt + ".png", image);
		// Upload the image into Parse Cloud

		return file;
	}

	public ParseObject addToWine(ParseFile file) {
		String wineNameTxt = wineName.getText().toString();
		String wineryTxt = winery.getText().toString();
		String yearTxt = year.getText().toString();
		int yearInt = Integer.parseInt(yearTxt);

		// Create a column named "ImageFile" and insert the image
		wine.put("imageFile", file);
		wine.put("name", wineNameTxt);
		wine.put("winary", wineryTxt);
		wine.put("year", yearInt);

		return wine;
	}

	public void addListenerOnRatingBar() {

		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);

		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

				Toast.makeText(NewEntryActivity.this,
						String.valueOf(ratingBar.getRating()),
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case IMAGE_PICK:
				this.imageFromGallery(resultCode, data);

				Toast.makeText(NewEntryActivity.this, "Image Selected",
						Toast.LENGTH_SHORT).show();
				break;
			case IMAGE_CAPTURE:
				this.imageFromCamera(resultCode, data);

				Toast.makeText(NewEntryActivity.this, "Image Selected",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Update the imageView with new bitmap
	 * 
	 * @param newImage
	 */
	private void updateImageView(Bitmap newImage) {
		BitmapProcessor bitmapProcessor = new BitmapProcessor(newImage, 600,
				600);

		image = bitmapProcessor.getBitmap();
		this.imageView.setImageBitmap(image);
	}

	/**
	 * Image result from camera
	 * 
	 * 
	 */
	private void imageFromCamera(int resultCode, Intent data) {
		this.updateImageView((Bitmap) data.getExtras().get("data"));
	}

	/**
	 * Image result from gallery
	 * 
	 * 
	 */
	private void imageFromGallery(int resultCode, Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();

		this.updateImageView(BitmapFactory.decodeFile(filePath));
	}

	/**
	 * Click Listener for selecting images from phone gallery
	 * 
	 * 
	 */
	class ImagePickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(Intent.createChooser(intent, "Pick Image"),
					IMAGE_PICK);

		}
	}

	/**
	 * Click listener for taking new picture
	 * 
	 * 
	 */
	private class TakePictureListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, IMAGE_CAPTURE);

		}
	}

	private class SaveImageCallback extends com.parse.SaveCallback {

		@Override
		public void done(ParseException arg0) {
			{
				if (arg0 == null) {
					addToWine(imageFile).saveInBackground(new SaveBasicWineCallback());
				} else {
					Log.e("SAVE Image", arg0.getLocalizedMessage(), arg0);
				}
			}
		}

	}

	private class SaveBasicWineCallback extends com.parse.SaveCallback {

		@Override
		public void done(ParseException arg0) {
			if (arg0 == null) {
				addToReview().saveInBackground(new SaveBasicReviewCallback());
			} else {
				Log.e("Save Basic Wine", arg0.getLocalizedMessage(), arg0);

			}
		}
	}

	private class SaveBasicReviewCallback extends com.parse.SaveCallback {

		@Override
		public void done(ParseException arg0) {
			if (arg0 == null) {
				addWineRelations().saveInBackground(new SaveWineRelationsCallback());

			} else {
				Log.e("Save basic review", arg0.getLocalizedMessage(), arg0);
			}
		}

	}

	private class SaveWineRelationsCallback extends com.parse.SaveCallback {

		@Override
		public void done(ParseException arg0) {
			if (arg0 == null) {
				addReviewRelations().saveInBackground(new SaveReviewRelationsCallback());
			} else {
				Log.e("Save wine relations", arg0.getLocalizedMessage(), arg0);
			}
		}

	}

	private class SaveReviewRelationsCallback extends com.parse.SaveCallback {
		@Override
		public void done(ParseException arg0) {
			if (arg0 == null) {
				Toast.makeText(NewEntryActivity.this, "Info Uploaded",
						Toast.LENGTH_SHORT).show();

				Intent second = new Intent(NewEntryActivity.this,
						WineListActivity.class);
				startActivity(second);
				finish();
			} else {
				Log.e("Save review relations", arg0.getLocalizedMessage(), arg0);
			}
		}

	}

}
