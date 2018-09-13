package com.ydtong.autoupdate.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.ydtong.autoupdate.AuConst;

public class HttpUtils {
	
	public static String BASIC_USER = "";
	public static String BASIC_PWD = "";

	private static final int CONNECT_TIMEOUT = 5 * 1000;
	private static final int READ_TIMEOUT = 30 * 1000;

	public static HttpResult sendGet(String reqUrl, Map<String, String> params) {
		HttpURLConnection conn = null;
		BufferedReader in = null;
		HttpResult result = new HttpResult();
		try {
			StringBuffer buf = new StringBuffer(reqUrl);
			if (params != null && !params.isEmpty()) {
				int index = 0;
				buf.append("?");
				Iterator<Entry<String, String>> entrys = params.entrySet()
						.iterator();
				while (entrys.hasNext()) {
					Entry<String, String> entry = entrys.next();
					buf.append(entry.getKey()).append("=")
							.append(entry.getValue());
					if (index != params.size() - 1) {
						buf.append("&");
					}
					index++;
				}
			}
			URL url = new URL(buf.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			if(!TextUtils.isEmpty(BASIC_USER) && !TextUtils.isEmpty(BASIC_PWD)) {
				String userpass = String.format("%s:%s", BASIC_USER, BASIC_PWD);
				conn.setRequestProperty("Authorization", "Basic " + new String(Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP)));
			}
			conn.connect();

			if (conn.getResponseCode() == 200) {
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line;
				String response = "";
				while ((line = in.readLine()) != null) {
					response += line;
				}
				result.setResult(response);
				result.setSuccess(true);

			} else {
				result.setSuccess(false);
				result.setResult("请求失败" + conn.getResponseCode());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
	}

	@SuppressLint("NewApi")
	public static HttpResult sendPost(String reqUrl, Map<String, String> params) {
		BufferedReader in = null;
		HttpResult result = new HttpResult();
		try {

			URL url = new URL(reqUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST必须设置如下两行
			conn.setDoInput(true);
			conn.setDoOutput(true);
			if(!TextUtils.isEmpty(BASIC_USER) && !TextUtils.isEmpty(BASIC_PWD)) {
				String userpass = String.format("%s:%s", BASIC_USER, BASIC_PWD);
				conn.setRequestProperty("Authorization", "Basic " + new String(Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP)));
			}
			// 传递的数据
			StringBuffer buf = new StringBuffer();
			if (params != null && !params.isEmpty()) {
				int index = 0;
				Iterator<Entry<String, String>> entrys = params.entrySet()
						.iterator();
				while (entrys.hasNext()) {
					Entry<String, String> entry = entrys.next();
					buf.append(entry.getKey()).append("=")
							.append(entry.getValue());
					if (index != params.size() - 1) {
						buf.append("&");
					}
					index++;
				}
			}
			// 获取URLConnection对象对应的输出流
			OutputStream os = conn.getOutputStream();
			os.write(buf.toString().getBytes(Charset.forName("GBK")));
			os.flush();
			if (conn.getResponseCode() == 200) {
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line;
				String response = "";
				while ((line = in.readLine()) != null) {
					response += line;
				}
				result.setResult(response);
				result.setSuccess(true);
			} else {
				result.setResult(false);
				result.setResult("请求失败" + conn.getResponseCode());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
	}

	/**
	 * 上传文件
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static HttpResult formUpload(String urlStr,
			Map<String, String> fileMap) {
		HttpResult result = new HttpResult();
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			String userpass = "ydserver:555555zj360";
			conn.setRequestProperty("Authorization", "Basic " + new String(Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP)));

			OutputStream out = new DataOutputStream(conn.getOutputStream());

			// file
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					/*
					 * MagicMatch match = Magic.getMagicMatch(file, false,
					 * true); String contentType = match.getMimeType();
					 */

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:multipart/form-data"
							+ "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			if (conn.getResponseCode() == 200) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					strBuf.append(line).append("\n");
				}
				result.setResult(strBuf.toString());
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setResult("请求失败" + conn.getResponseCode());
			}
			reader.close();
			reader = null;
		} catch (Exception e) {
			Log.e("HttpUtils", "formUpload", e);
			result.setSuccess(false);
			result.setResult("请求异常");
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return result;
	}
	
	
	/**
	 * 下载文件
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static HttpResult formDownload(String urlStr, String sdPath, Handler handle) {
		HttpResult result = new HttpResult();
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			conn.setUseCaches(false);
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Connection", "Keep-Alive");
//			conn.setRequestProperty("User-Agent",
//					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
//			conn.setRequestProperty("Content-Type",
//					"multipart/form-data; boundary=" + BOUNDARY);
//			OutputStream os = new FileOutputStream(file)
			
//			BufferedInputStream input = new BufferedInputStream();
			int fileLen = conn.getContentLength();
			try {
				writeToFile(conn.getInputStream(), sdPath, fileLen, AuConst.APK_NAME, handle);
				result.setSuccess(true);
				result.setResult("下载成功");
			} catch (Exception e) {
				result.setSuccess(false);
				result.setResult("下载失败");
			}
		} catch (Exception e) {
			Log.e("HttpUtils", "formUpload", e);
			result.setSuccess(false);
			result.setResult("请求异常");
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return result;
	}
	
	/**
	 * 写入文件
	 * 
	 * @param inputStream
	 *            下载文件的字节流对象
	 * @param sdpath
	 *            文件的存放目录
	 * @throws Exception 
	 */
	public static void writeToFile(InputStream inputStream, String sdpath, int fileLen, String fileName, Handler handle) throws Exception {
		OutputStream ouput = null;
		try {
			createSDDir(sdpath);
			// 在指定目录创建一个空文件并获取文件对象
			File file = createSDFile(sdpath + "/" + fileName);
			// 获取一个写入文件流对象
			ouput = new FileOutputStream(file);
			// 创建一个4*1024大小的字节数组，作为循环读取字节流的临时存储空

			byte buffer[] = new byte[1024];
			int size = 0;
			int len = 0;
			int no = 0;
			// 循环读取下载的文件到buffer对象数组中
			while ((size = inputStream.read(buffer)) != -1) {
				len += size;
				// 把文件写入到文件
				ouput.write(buffer, 0 , size);
				if(handle != null) {
					if(no == 50 && len != fileLen) {
						Message msg = handle.obtainMessage();
						msg.what = 3;
						msg.arg1 = (len * 100) / fileLen;
						handle.sendMessage(msg);
						no = 0;
					} else if(len == fileLen) {
						handle.sendEmptyMessage(4);
					}
				}
				no++;
			}
		} catch (Exception e) {
			Log.e("HttpUtils", "writeToFile", e);
			throw new Exception(e);
		} finally {
			try {
				// 关闭写入流
				ouput.close();
				inputStream.close();
			} catch (Exception e) {
				// TODO:handle exception
				e.printStackTrace();
			}
		}
	}

	private static File createSDFile(String sdPath) {
		File file = new File(sdPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	private static File createSDDir(String sdPath) {
		File file = new File(sdPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

}
