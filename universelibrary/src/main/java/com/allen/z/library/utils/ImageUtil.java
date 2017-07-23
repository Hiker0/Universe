package com.allen.z.library.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

	/**
	 * ����Դ�л�ȡBitmap
	 * 
	 * @param act
	 * @param resId
	 * @return
	 */
	public static Bitmap getBitmapFromResources(Activity act, int resId) {
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	/**
	 * Bitmap ת���� byte ����
	 * 
	 * @param bmp
	 * @return
	 */
	public static byte[] bitmap2Byte(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * ����ת���� Bitmap
	 * 
	 * @param buffer
	 * @return
	 */
	public static Bitmap byte2Bitmap(byte[] buffer) {
		return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
	}

	/**
	 * Bitmap ת���� Drawable
	 * 
	 * @param bmp
	 * @return
	 */

	public static Drawable bitmap2Drawable(Bitmap bmp) {
		return new BitmapDrawable(bmp);
	}

	/**
	 * BitmapDrawable ת���� Bitmap
	 * 
	 * @param drawable
	 * @return
	 */

	public static Bitmap drawable2Bitmap(BitmapDrawable drawable) {
		return drawable.getBitmap();
	}

	/**
	 * ��Drawableת��ΪBitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		
		if(width <= 0 || height <= 0){
			return null;
		}
		
//		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
//				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//				: Bitmap.Config.RGB_565);

		Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	public static void saveBitmap(Bitmap mBitmap, File file) throws IOException {

		file.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	 
	  public void DrawableToFile(Integer PicID, String filename ) { 
		  
	  }
 
	         

}
