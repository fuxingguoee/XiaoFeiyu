package com.il360.xiaofeiyu.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;

/**
 * @author wangjie
 * @version 创建时间：2013-3-15 上午11:35:24
 */
public class FileUtil {
	/**
	 * 创建指定目录
	 * @author wangjie
	 * @param pathName 要创建的目录名
	 */
	public static void createDir(String pathName){
		File dir = new File(pathName);
		dir.mkdirs();
	}
	/**
	 * 创建一个新文件
	 * @param pathFileName
	 * @return 
	 */
	public static File createFile(String pathFileName){
		File file = null;
		String path = pathFileName.substring(0, pathFileName.lastIndexOf("/"));
		try {
			createDir(path);
			file = new File(pathFileName);
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 * 把bitmap保存到sd卡上
	 * @param path
	 * @param fileName
	 * @param bm
	 * @return 0:成功; 1:失败
	 */
	public static int saveBitmap2SD(String pathFileName, Bitmap bm){
		File file = null;
		try {
			file = createFile(pathFileName);
		    FileOutputStream out = new FileOutputStream(file.getPath());
		    bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
		    return 0;
		} catch (Exception e) {
		    e.printStackTrace();
		    return 1;
		}
	}
	
	
	/**
     * 递归删除文件和文件夹
     * @param file    要删除的根目录
     */
	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}
    
	/**
	 * String --> InputStream
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}
	
	public static String escapeFilename(String filename){
		return filename.replaceAll("[/\\:?''<>|/\n]", "_");
	}
	
	public static String cosFileName(String str, Integer userId) {
		String fn = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());
		fn = str + "_" + userId + "_" + formatter.format(curDate);
		return fn;
	}
}
