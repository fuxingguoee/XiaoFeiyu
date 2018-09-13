package com.il360.xiaofeiyu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.il360.xiaofeiyu.common.GlobalPara;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;


/** 
 * 加载大图片工具类：解决android加载大图片时报OOM异常 
 * 解决原理：先设置缩放选项，再读取缩放的图片数据到内存，规避了内存引起的OOM 
 * @author: Terry  
 
 * @time:2015/01/02 
 */  
public class BitmapUtil {  
  
    public static final int UNCONSTRAINED = -1;  
      
    @SuppressLint("NewApi")
	public static String getimgpath(Intent data, Activity act)
    {
    	 final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
    	 Uri originalUri = data.getData(); 
    	    // DocumentProvider  
    	    if (isKitKat && DocumentsContract.isDocumentUri(act.getApplicationContext(), originalUri)) {  
    	        // ExternalStorageProvider  
    	        if (isExternalStorageDocument(originalUri)) {  
    	            final String docId = DocumentsContract.getDocumentId(originalUri);  
    	            final String[] split = docId.split(":");  
    	            final String type = split[0];  
    	  
    	            if ("primary".equalsIgnoreCase(type)) {  
    	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
    	            }  
    	  
    	            // TODO handle non-primary volumes  
    	        }  
    	        // DownloadsProvider  
    	        else if (isDownloadsDocument(originalUri)) {  
    	  
    	            final String id = DocumentsContract.getDocumentId(originalUri);  
    	            final Uri contentUri = ContentUris.withAppendedId(  
    	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
    	  
    	            return getDataColumn(act.getApplicationContext(), contentUri, null, null);  
    	        }  
    	        // MediaProvider  
    	        else if (isMediaDocument(originalUri)) {  
    	            final String docId = DocumentsContract.getDocumentId(originalUri);  
    	            final String[] split = docId.split(":");  
    	            final String type = split[0];  
    	  
    	            Uri contentUri = null;  
    	            if ("image".equals(type)) {  
    	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
    	            } else if ("video".equals(type)) {  
    	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
    	            } else if ("audio".equals(type)) {  
    	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
    	            }  
    	  
    	            final String selection = "_id=?";  
    	            final String[] selectionArgs = new String[] {  
    	                    split[1]  
    	            };  
    	  
    	            return getDataColumn(act.getApplicationContext(), contentUri, selection, selectionArgs);  
    	        }  
    	    }  
    	    // MediaStore (and general)  
    	    else if ("content".equalsIgnoreCase(originalUri.getScheme())) {  
    	        return getDataColumn(act.getApplicationContext(), originalUri, null, null);  
    	    }  
    	    // File  
    	    else if ("file".equalsIgnoreCase(originalUri.getScheme())) {  
    	        return originalUri.getPath();  
    	    }  
    	  
    	    return null;  
    }
    
	    /* 
	  * 获得设置信息 
	  */  
	 public static Options getOptions(String path){  
		  Options options = new Options();  
		  options.inJustDecodeBounds = true;//只描边，不读取数据   
		  BitmapFactory.decodeFile(path, options);  
		  return options;  
	 }  
	 
	 /** 
	  * Get the value of the data column for this Uri. This is useful for 
	  * MediaStore Uris, and other file-based ContentProviders. 
	  * 
	  * @param context The context. 
	  * @param uri The Uri to query. 
	  * @param selection (Optional) Filter used in the query. 
	  * @param selectionArgs (Optional) Selection arguments used in the query. 
	  * @return The value of the _data column, which is typically a file path. 
	  */  
	 public static String getDataColumn(Context context, Uri uri, String selection,  
	         String[] selectionArgs) {  
	   
	     Cursor cursor = null;  
	     final String column = "_data";  
	     final String[] projection = {  
	             column  
	     };  
	   
	     try {  
	         cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                 null);  
	         if (cursor != null && cursor.moveToFirst()) {  
	             final int column_index = cursor.getColumnIndexOrThrow(column);  
	             return cursor.getString(column_index);  
	         }  
	     } finally {  
	         if (cursor != null)  
	             cursor.close();  
	     }  
	     return null;  
	 }  
	 
	 /** 
	  * @param uri The Uri to check. 
	  * @return Whether the Uri authority is ExternalStorageProvider. 
	  */  
	 public static boolean isExternalStorageDocument(Uri uri) {  
	     return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	 }  
	   
	 /** 
	  * @param uri The Uri to check. 
	  * @return Whether the Uri authority is DownloadsProvider. 
	  */  
	 public static boolean isDownloadsDocument(Uri uri) {  
	     return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	 }  
	   
	 /** 
	  * @param uri The Uri to check. 
	  * @return Whether the Uri authority is MediaProvider. 
	  */  
	 public static boolean isMediaDocument(Uri uri) {  
	     return "com.android.providers.media.documents".equals(uri.getAuthority());  
	 }  
	   
   
	 /** 
	  * 获得图像 
	  * @param path 
	  * @param options 
	  * @return 
	  * @throws FileNotFoundException 
	  */  
	public static Bitmap getBitmapByPath(String path, Options options,
			int screenWidth, int screenHeight) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		FileInputStream in = null;
		in = new FileInputStream(file);
		if (options != null) {
			Rect r = getScreenRegion(screenWidth, screenHeight);
			int w = r.width();
			int h = r.height();
			int maxSize = w > h ? w : h;
			int inSimpleSize = computeSampleSize(options, maxSize, w * h);
			options.inSampleSize = inSimpleSize; // 设置缩放比例
			options.inJustDecodeBounds = false;
		}
		Bitmap b = BitmapFactory.decodeStream(in, null, options);
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
	   
   
      
	 private static Rect getScreenRegion(int width , int height) {  
		 return new Rect(0,0,width,height);  
	 }  
  
  
	 /** 
	  * 获取需要进行缩放的比例，即options.inSampleSize 
	  * @param options 
	  * @param minSideLength 
	  * @param maxNumOfPixels 
	  * @return 
	  */  
	 public static int computeSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        int initialSize = computeInitialSampleSize(options, minSideLength,  
                maxNumOfPixels);  
  
        int roundedSize;  
        if (initialSize <= 8) {  
            roundedSize = 1;  
            while (roundedSize < initialSize) {  
                roundedSize <<= 1;  
            }  
        } else {  
            roundedSize = (initialSize + 7) / 8 * 8;  
        }  
  
        return roundedSize;  
    }  
  
    private static int computeInitialSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        double w = options.outWidth;  
        double h = options.outHeight;  
  
        int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 :  
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
        int upperBound = (minSideLength == UNCONSTRAINED) ? 128 :  
                (int) Math.min(Math.floor(w / minSideLength),  
                Math.floor(h / minSideLength));  
  
        if (upperBound < lowerBound) {  
            // return the larger one when there is no overlapping zone.   
            return lowerBound;  
        }  
  
        if ((maxNumOfPixels == UNCONSTRAINED) &&  
                (minSideLength == UNCONSTRAINED)) {  
            return 1;  
        } else if (minSideLength == UNCONSTRAINED) {  
            return lowerBound;  
        } else {  
            return upperBound;  
        }  
    }
    
    //保存图片到相册
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), GlobalPara.getCosName());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
    	}
        
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
    				file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,	Uri.fromFile(new File(file.getPath()))));
        file.delete();
    }
    
	public static String getAbsolutePath(Context context, Bitmap bmp) {
		File appDir = new File(Environment.getExternalStorageDirectory(), GlobalPara.getCosName());
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
		return file.getAbsolutePath();
	}
	
	
	
	// 声明称为静态变量有助于调用
	public static byte[] readImage(String path) throws Exception {
		URL url = new URL(path);
		// 记住使用的是HttpURLConnection类
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// 如果运行超过5秒会自动失效 这是android规定
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream = conn.getInputStream();
		// 调用readStream方法
		return readStream(inStream);
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		// 把数据读取存放到内存中去
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
	
	/** 
     * 通过uri获取图片并进行压缩 
     * 
     * @param uri 
     */  
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);  
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();  
        onlyBoundsOptions.inJustDecodeBounds = true;  
        onlyBoundsOptions.inDither = true;//optional  
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional  
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);  
        input.close();  
        int originalWidth = onlyBoundsOptions.outWidth;  
        int originalHeight = onlyBoundsOptions.outHeight;  
        if ((originalWidth == -1) || (originalHeight == -1))  
            return null;  
        //图片分辨率以480x800为标准  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (originalWidth / ww);  
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (originalHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        //比例压缩  
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
        bitmapOptions.inSampleSize = be;//设置缩放比例  
        bitmapOptions.inDither = true;//optional  
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional  
        input = ac.getContentResolver().openInputStream(uri);  
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);  
        input.close();  
   
        return compressImage(bitmap);//再进行质量压缩  
    }  
    
    /** 
     * 质量压缩方法 
     * 
     * @param image 
     * @return 
     */  
    public static Bitmap compressImage(Bitmap image) {  
    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩  
            baos.reset();//重置baos即清空baos  
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
	
}  
