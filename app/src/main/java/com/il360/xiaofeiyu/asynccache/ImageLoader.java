package com.il360.xiaofeiyu.asynccache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.il360.xiaofeiyu.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * @author www
 * @version 创建时间：2012-10-14 下午7:30:07
 */
public class ImageLoader {
	private static ImageLoader imageLoader = new ImageLoader();

	private int defRequiredSize = 70;
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	ExecutorService executorService;
	private Bitmap bmp;

	private ImageLoader() {
		fileCache = new FileCache(new Activity());
		executorService = Executors.newFixedThreadPool(5);
	}

	// 单例模式
	public static ImageLoader getInstances() {
		return imageLoader;
	}

	public void setDefRequiredSize(int defRequiredSize) {
		this.defRequiredSize = defRequiredSize;
	}

	// 当进入listview时默认的图片，可换成你自己的默认图片
	final int stub_id = R.drawable.ic_image404;

	// 最主要的方法
	public void DisplayImage(String url, ImageView imageView, int requiredSize) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找

		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView, requiredSize);
			imageView.setImageResource(stub_id);
		}
	}

	public Bitmap DisplayImage(String url) {
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			return bitmap;
		else {
			// 若没有的话则开启新线程加载图片
			queueBitmap(url, defRequiredSize);
			return bmp;
		}
	}

	public Bitmap DisplayImage(String url, int requiredSize) {
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			return bitmap;
		else {
			// 若没有的话则开启新线程加载图片
			queueBitmap(url, requiredSize);
			return bmp;
		}
	}

	public void DisplayImage(String url, ImageView imageView) {
		DisplayImage(url, imageView, defRequiredSize);
	}

	private void queuePhoto(String url, ImageView imageView, int requiredSize) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p, requiredSize));
	}

	private Bitmap getBitmap(String url, int requiredSize) {
		File f = fileCache.getFile(url);

		// 先从文件缓存中查找是否有
		Bitmap b = decodeFile(f, requiredSize);
		if (b != null)
			return b;

		// 最后从指定的url中下载图片
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f, requiredSize);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f, int requiredSize) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.

			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;
		int requiredSize;

		PhotosLoader(PhotoToLoad photoToLoad, int requiredSize) {
			this.photoToLoad = photoToLoad;
			this.requiredSize = requiredSize;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url, requiredSize);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	// ylq 2016-3-18
	//用于根据URL获取bitmap
	private class BitmapToLoad {
		public String url;

		public BitmapToLoad(String u) {
			url = u;
		}
	}

	private void queueBitmap(String url, int requiredSize) {
		BitmapToLoad p = new BitmapToLoad(url);
		executorService.submit(new BitmapsLoader(p, requiredSize));
	}

	class BitmapsLoader implements Runnable {
		BitmapToLoad photoToLoad;
		int requiredSize;

		BitmapsLoader(BitmapToLoad photoToLoad, int requiredSize) {
			this.photoToLoad = photoToLoad;
			this.requiredSize = requiredSize;
		}

		@Override
		public void run() {
			bmp = getBitmap(photoToLoad.url, requiredSize);
			memoryCache.put(photoToLoad.url, bmp);
		}
	}
}
