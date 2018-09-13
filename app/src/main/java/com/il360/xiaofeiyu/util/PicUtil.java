package com.il360.xiaofeiyu.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

public class PicUtil {
	/**
	 * 获取裁剪图片方法实现的Intent return intent
	 * 
	 * @param uri
	 */
	public static Intent getCutPictureIntent(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		//判断版本大于等于7.0
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * 将bitmap转Base64 utf-8
	 * */
	public static String bitmaptoBase64(Bitmap bitmap) {
		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				bStream.flush();
				bStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 图片压缩 size 大小kb
	 * */
	public static Bitmap compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
				baos.toByteArray().length);// 把ByteArrayInputStream数据生成图片
		if (!image.isRecycled()) {
			image.recycle();// 释放资源
		}
		try {
			baos.flush();
			baos.close();
		} catch (Exception e) {
		}
		return bitmap;
	}

	/**
	 * 把bitmap转换成String
	 * 
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath) {

		Bitmap bm = getSmallBitmap(filePath);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		
		return Base64.encodeToString(b, Base64.DEFAULT);
		
	}
	
	/**
	 * 把bitmap转换成String
	 * 
	 * @param filePath
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String bitmapToString(Bitmap bm) throws UnsupportedEncodingException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale  
        int options = 100;  
        // Store the bitmap into output stream(no compress)  
        bm.compress(Bitmap.CompressFormat.JPEG, options, os);    
        // Compress by loop  
        while (os.toByteArray().length / 1024 > 38) {  
            // Clean up os  
            os.reset();  
            // interval 10  
            options -= 10;  
            bm.compress(Bitmap.CompressFormat.JPEG, options, os);  
        }  
        byte[] b = os.toByteArray();
		String picStr = Base64.encodeToString(b, Base64.DEFAULT);
		return URLEncoder.encode(picStr, "UTF-8");
		
	}
	
	/**
	 * 把bitmap转换成String
	 * 
	 * @param filePath
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String bitmapToString(Bitmap bm , int size) throws UnsupportedEncodingException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale  
        int options = 100;  
        // Store the bitmap into output stream(no compress)  
        bm.compress(Bitmap.CompressFormat.JPEG, options, os);    
        // Compress by loop  
        while (os.toByteArray().length / 1024 > size) {  
            // Clean up os  
            os.reset();  
            // interval 10  
            if(options > 10){
            	options -= 10;  
            } 
            bm.compress(Bitmap.CompressFormat.JPEG, options, os);  
        }  
        byte[] b = os.toByteArray();
		String picStr = com.il360.xiaofeiyu.util.Base64.encode(b);
		return picStr;
		
	}
	
	 /**  
     * string转成bitmap  
     *   
     * @param st  
     */  
	public static Bitmap convertStringToIcon(String st) {
		// OutputStream out;
		Bitmap bitmap = null;
		try {
			// out = new FileOutputStream("/sdcard/aa.jpg");
			byte[] bitmapArray;
			bitmapArray = Base64.decode(st, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	

	
	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 320);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		
		options.inPreferredConfig = Config.RGB_565;
		
		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		
		int degree = readPictureDegree(filePath);

		if (degree != 0) {// 旋转照片角度
			bm = rotateBitmap(bm, degree);
		}
		return bm;
	}

	/**
	 * 根据路径删除图片
	 * 
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 添加到图库
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取保存 隐患检查的图片文件夹名称
	 * 
	 * @return
	 */
	public static String getAlbumName() {
		return "sheguantong";
	}
	
	/*
	 * 压缩图片，处理某些手机拍照角度旋转的问题
	 */
	public static String compressImage(Context context, String filePath,
			String fileName, int q) throws FileNotFoundException {

		Bitmap bm = getSmallBitmap(filePath);

		// File imageDir = SDCardUtils.getImageDir(context);
		File imageDir = PicUtil.getAlbumDir();

		File outputFile = new File(imageDir, fileName);

		FileOutputStream out = new FileOutputStream(outputFile);
		
		bm.compress(Bitmap.CompressFormat.JPEG, q, out);
		
		return outputFile.getPath();
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}
	

	public static String uri2Path(Uri uri, Context context) {
		int actual_image_column_index;
		String img_path;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
		actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		img_path = cursor.getString(actual_image_column_index);
		return img_path;
	}
	
	/** 
     * Compress by quality,  and generate image to the path specified 
     *  
     * @param image 
     * @param outPath 
     * @param maxSize target will be compressed to be smaller than this size.(kb) 
     * @throws IOException  
     */  
    public static void compressImageBySize(Bitmap image, int maxSize) throws IOException {  
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale  
        int options = 100;  
        // Store the bitmap into output stream(no compress)  
        image.compress(Bitmap.CompressFormat.JPEG, options, os);    
        // Compress by loop  
        while (os.toByteArray().length / 1024 > maxSize) {  
            // Clean up os  
            os.reset();  
            // interval 10  
            options -= 10;  
            image.compress(Bitmap.CompressFormat.JPEG, options, os);  
        }  
    }  
}
