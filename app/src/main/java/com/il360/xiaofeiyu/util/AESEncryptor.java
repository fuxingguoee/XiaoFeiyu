package com.il360.xiaofeiyu.util;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

/**
 * @author carlos carlosk@163.com
 * @version 创建时间：2012-5-17 上午9:48:35 类说明
 */

public class AESEncryptor {

	public static final String VIPARA = "1112131415161718";
	public static final String BM = "UTF-8";
	public static final String KEY = "8546958741365589";
	
//	public static String encrypt(String dataPassword, String cleartext) {
//		dataPassword = KEY;
//		IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
//		
//		SecretKeySpec key = new SecretKeySpec((keySet(dataPassword)).getBytes(), "AES");
//		byte[] encryptedData = null;
//		try {
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
//			encryptedData = cipher.doFinal(cleartext.getBytes(BM));
//		} catch (Exception e) {
//			Log.e("AES", "ecrypt the " + cleartext + " fail");
//		}
//		return Base64.encode(encryptedData);
//	}
	
	public static String encrypt(String cleartext) {
		IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
		
		SecretKeySpec key = new SecretKeySpec((keySet(KEY)).getBytes(), "AES");
		byte[] encryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			encryptedData = cipher.doFinal(cleartext.getBytes(BM));
		} catch (Exception e) {
			Log.e("AES", "ecrypt the " + cleartext + " fail");
		}
		return Base64.encode(encryptedData);
	}
	
	

	public static String decrypt(String dataPassword, String encrypted) {
		byte[] byteMi = Base64.decode(encrypted);
		IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
		SecretKeySpec key = new SecretKeySpec((keySet(dataPassword)).getBytes(), "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte[] decryptedData = cipher.doFinal(byteMi);
			return new String(decryptedData, BM);
		} catch (Exception e) {
			Log.e("AES", "decrypt the " + encrypted + " fail");
			return encrypted;
		}
	}
	
	private static String keySet(String key) {
		String result = "";
		if (key.length() < 16) {
			result = key.concat(key);
			return keySet(result);
		} else {
			return key.substring(0, 16);
		}
	}
}