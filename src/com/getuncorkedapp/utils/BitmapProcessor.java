package com.getuncorkedapp.utils;

import android.graphics.Bitmap;

public class BitmapProcessor {
	private Bitmap _bitmap;
	
	public Bitmap getBitmap() {
		return this._bitmap;
	}
	
	public BitmapProcessor(Bitmap bitmap) {
		this._bitmap = bitmap;
	}
	
	public BitmapProcessor(Bitmap bitmap, int maxWidth, int maxHeight) {
		this._bitmap = bitmap;
		
		this.resizeIfSmallerThan(maxWidth, maxHeight);
	}
	
	
	public Bitmap resizeIfSmallerThan(int maxWidth, int maxHeight) {
		int   originalWidth 	= this._bitmap.getWidth();
		int   originalHeight 	= this._bitmap.getHeight();
		int   newWidth 			= (int) (originalWidth * 2.95);
		int   newHeight 		= (int) (originalHeight * 2.95);		

		this._bitmap = Bitmap.createScaledBitmap(this._bitmap, newWidth, newHeight, true);

		return this._bitmap;
	}
	
}
