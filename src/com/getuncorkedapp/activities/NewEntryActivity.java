package com.getuncorkedapp.activities;

import com.getuncorkedapp.utils.BitmapProcessor;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.getuncorkedapp.R;

public class NewEntryActivity extends Activity {

	private EditText wineName;
	private EditText winery;
	private EditText year;
	private EditText description;
	private Button add;
	private Button cancel;
	private RatingBar ratingBar;

	// Image variables
	private ImageView imageView;
	private Button buttonNewPic;
	private Button buttonImage;

	private Bitmap image;

	private static final int IMAGE_PICK = 1;
	private static final int IMAGE_CAPTURE = 2;

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

		imageView = (ImageView) findViewById(R.id.imageView);
		buttonNewPic = (Button) findViewById(R.id.button_camera);
		buttonImage = (Button) findViewById(R.id.button_from_phone);

		buttonImage.setOnClickListener(new ImagePickListener());
		buttonNewPic.setOnClickListener(new TakePictureListener());

		addListenerOnRatingBar();

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent second = new Intent(NewEntryActivity.this,
						LoginActivity.class);
				startActivity(second);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent second = new Intent(NewEntryActivity.this,
						LoginActivity.class);
				startActivity(second);
			}
		});
	}

	public void addListenerOnRatingBar() {

		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);

		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

				Toast.makeText(NewEntryActivity.this,
						String.valueOf(ratingBar.getRating()),
						Toast.LENGTH_SHORT).show();

				// Parse.add(String.valueOf(rating));

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
		BitmapProcessor bitmapProcessor = new BitmapProcessor(newImage, 1000,
				1000, 90);

		this.image = bitmapProcessor.getBitmap();
		this.imageView.setImageBitmap(this.image);
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
	class TakePictureListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, IMAGE_CAPTURE);


		}
	}

}
