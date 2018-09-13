package com.il360.xiaofeiyu.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;

public class FileHelper {
	private Context context;
	/** SD卡是否存在 **/
	private boolean hasSD = false;
	/** SD卡的路径 **/
	private String SDPATH;
	/** 当前程序包的路径 **/
	private String FILESPATH;

	public FileHelper(Context context) {
		this.context = context;
		hasSD = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		SDPATH = Environment.getExternalStorageDirectory().getPath();
		FILESPATH = this.context.getFilesDir().getPath();
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + "//" + fileName);
		if (!file.exists()) {
			file.createNewFile();
		} 
		return file;
	}

	/**
	 * 删除SD卡上的文件
	 * 
	 * @param fileName
	 */
	public boolean deleteSDFile(String fileName) {
		File file = new File(SDPATH + "//" + fileName);
		if (file == null || !file.exists() || file.isDirectory())
			return false;
		return file.delete();
	}

	/**
	 * 写入内容到SD卡中的txt文本中 str为内容
	 */
	public String writeSDFile(String str, String fileName) {
//		try {
//			FileWriter fw = new FileWriter(SDPATH + "//" + fileName);
//			File f = new File(SDPATH + "//" + fileName);
//			fw.write(str);
//			FileOutputStream os = new FileOutputStream(f);
//			DataOutputStream out = new DataOutputStream(os);
//			out.writeShort(2);
//			out.writeUTF("GBK");
//			System.out.println(out);
//			fw.flush();
//			fw.close();
//			System.out.println(fw);
//		} catch (Exception e) {
//		}
		
		
		try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    fileName);
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(str);
            bw.flush();
            bw.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
		
		
//		try {
//			OutputStreamWriter write = null;
//			BufferedWriter out = null;
//			if (fileName != null) {
//				try { // new FileOutputStream(fileName, true) 第二个参数表示追加写入
//					write = new OutputStreamWriter(new FileOutputStream(fileName), Charset.forName("gbk"));// 一定要使用gbk格式
//					out = new BufferedWriter(write);
//				} catch (Exception e) {
//				}
//			}
//			out.write(str);
//			out.flush();
//			out.close();
//		} catch (Exception e) {
//		}
	}

	/**
	 * 读取SD卡中文本文件
	 * 
	 * @param fileName
	 * @return
	 */
	public String readSDFile(String fileName) {
		File file = new File(SDPATH + "//" + fileName);
//		try {
//			if (file.isFile() && file.exists()) {
//				InputStreamReader isr = new InputStreamReader(
//						new FileInputStream(file), Charset.forName("gbk"));
//				BufferedReader br = new BufferedReader(isr);
//				String lineTxt = null;
//				StringBuffer sb = new StringBuffer();
//				while ((lineTxt = br.readLine()) != null) {
//					sb.append(lineTxt);
//				}
//				br.close();
//				return sb.toString();
//			} else {
//				System.out.println("文件不存在!");
//			}
//		} catch (Exception e) {
//			System.out.println("文件读取错误!");
//		}
//		return null;
		
		try {
//			File file = new File(fileName);
			if (file.isFile() && file.exists()) {
				InputStreamReader isr = new InputStreamReader(
						new FileInputStream(file), "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String lineTxt = null;
				StringBuffer sb = new StringBuffer();
				while ((lineTxt = br.readLine()) != null) {
					sb.append(lineTxt);
				}
				br.close();
				return sb.toString();
			} else {
				System.out.println("文件不存在!");
			}
		} catch (Exception e) {
			System.out.println("文件读取错误!");
		}
		return null;
		
		
		
	}

	public String getFILESPATH() {
		return FILESPATH;
	}

	public String getSDPATH() {
		return SDPATH;
	}

	public boolean hasSD() {
		return hasSD;
	}
	
	
	
	/**
     * 读取assets下的txt文件，返回utf-8 String
     * @param context
     * @param fileName 不包括后缀
     * @return
     */
    public static String readAssetsTxt(Context context,String fileName){
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName+".txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
           return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }
}
