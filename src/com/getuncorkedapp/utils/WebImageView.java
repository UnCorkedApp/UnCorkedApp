package com.getuncorkedapp.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by loran on 4/28/14.
 */
public class WebImageView extends ImageView {
	
	Bitmap bitmap;
	
    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	public void setImageUrl(String url) {
		new RetriveImageTask().execute(url);
    }
    
    class RetriveImageTask extends AsyncTask<String, Void, Bitmap> {
		
		protected void onPostExecute(Bitmap result) {
			WebImageView.this.setImageBitmap(result);
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			try {
	            URL url = new URL((String) urls[0]);
	            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return null;
		}
    	
    }
}