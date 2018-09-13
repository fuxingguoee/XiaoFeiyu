package com.il360.xiaofeiyu.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.il360.xiaofeiyu.common.MyApplication;
import com.il360.xiaofeiyu.common.URLFactory;
import com.il360.xiaofeiyu.common.Variables;

import android.util.Log;


/* 
 * 此类用来发送HTTP请求 
 * */  
public class HttpRequestUtil {  
	
	public static TResult<Boolean, String> sendGetRequest(UrlEnum url, String relativeUrl, Map<String, String> params){
		
		try {
			if(!netWorkNormal()) {
				return new TResult<Boolean, String>(false, "网络请求失败");
			}
			UsernamePasswordCredentials creds = getCredentials(url);
			String absoluteUrlStr = getAbsoluteUrl(url, relativeUrl);
			URL absoluteUrl= new URL(absoluteUrlStr);
			final CloseableHttpClient httpClient = getApacheHttpClient(absoluteUrl, creds);
			
			StringBuilder buf = new StringBuilder(absoluteUrlStr);  
	        Set<Entry<String, String>> entrys = null;  
	        // 如果是GET请求，则请求参数在URL中  
	        if (params != null && !params.isEmpty()) {  
	            entrys = params.entrySet();  
	            for (Map.Entry<String, String> entry : entrys) {  
	                buf.append("&").append(entry.getKey()).append("=")  
	                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	            }  
	        }  
			final HttpGet post = new HttpGet(buf.toString());

            ExecutorService executor = Executors.newSingleThreadExecutor();
            FutureTask<HttpResponse> futureTask = new FutureTask<HttpResponse>(
					new Callable<HttpResponse>() {// 使用Callable接口作为构造参数
						public HttpResponse call() {
							HttpResponse response = null;
							try {
								response = httpClient.execute(post);
							} catch (Exception e) {
							}
							return response;
						}
					});
			executor.execute(futureTask);
			
			HttpResponse response = null;
			try {
				response = futureTask.get(40000, TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				futureTask.cancel(true);
			} finally {
				executor.shutdown();
			}
			
			if(response != null) {
				if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatusLine().getStatusCode());
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(response.getEntity().getContent())));
				String output;
				StringBuilder result = new StringBuilder();
				while ((output = br.readLine()) != null) {
					result.append(output);
					Log.d("sendGetRequest", output);
				}
				return new TResult<Boolean, String>(true, result.toString());
			} else {
				return new TResult<Boolean, String>(false, "网络不给力");
			}
			
		} catch (Exception e) {
			Log.e("HttpRequestUtil", "sendGetRequest", e);
			return new TResult<Boolean, String>(false, "网络不给力(01)");
		}
	}
	
	public static TResult<Boolean, String> sendPostRequest(UrlEnum url, String relativeUrl, Map<String, String> params) {
		try {
			if(!netWorkNormal()) {
				return new TResult<Boolean, String>(false, "网络请求失败");
			}
			UsernamePasswordCredentials creds = getCredentials(url);
			String absoluteUrlStr = getAbsoluteUrl(url, relativeUrl);
			URL absoluteUrl= new URL(absoluteUrlStr);
			final CloseableHttpClient httpClient = getApacheHttpClient(absoluteUrl, creds);
			
			final HttpPost post = new HttpPost(absoluteUrlStr);
			List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
			Set<Entry<String, String>> entrys = null;  
	        if (params != null && !params.isEmpty()) {  
	            entrys = params.entrySet();  
	            for (Map.Entry<String, String> entry : entrys) {
	            	pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	            }  
	        }  
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
			entity.setContentType("application/x-www-form-urlencoded");
			entity.setContentEncoding("UTF-8");
            post.setEntity(entity);  
			
            ExecutorService executor = Executors.newSingleThreadExecutor();
            FutureTask<HttpResponse> futureTask = new FutureTask<HttpResponse>(
					new Callable<HttpResponse>() {// 使用Callable接口作为构造参数
						public HttpResponse call() {
							HttpResponse response = null;
							try {
								response = httpClient.execute(post);
							} catch (Exception e) {
							}
							return response;
						}
					});
			executor.execute(futureTask);
            
			HttpResponse response = null;
			try {
				response = futureTask.get(400000, TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				futureTask.cancel(true);
			} finally {
				executor.shutdown();
			}
			if(response != null) {
				if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatusLine().getStatusCode());
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(response.getEntity().getContent())));
				String output;
				StringBuilder result = new StringBuilder();
				while ((output = br.readLine()) != null) {
					result.append(output);
				}
				return new TResult<Boolean, String>(true, result.toString());
			} else {
				return new TResult<Boolean, String>(false, "网络不给力");
			}
			
		} catch (Throwable e) {
			Log.e("HttpRequestUtil", "sendPostRequest", e);
			return new TResult<Boolean, String>(false, "网络不给力(01)");
		}
	}
	
	private static String getAbsoluteUrl(UrlEnum url, String relativeUrl) {
		if (url.equals(UrlEnum.BIZ_URL)) {
			return URLFactory.DIANXIN_URL + relativeUrl + ".html?_type=json";
		} else if (url.equals(UrlEnum.LOG_URL)) {
			return URLFactory.LOG_URL + relativeUrl + ".html?_type=json";
		} else {
			return URLFactory.DIANXIN_URL + relativeUrl + ".html?_type=json";
		}
	}

	private static UsernamePasswordCredentials getCredentials(UrlEnum url) {
		if (url.equals(UrlEnum.BIZ_URL)) {
			return new UsernamePasswordCredentials(Variables.WEBSERVICE_USER, Variables.WEBSERVICE_PWD);
		} else if (url.equals(UrlEnum.LOG_URL)) {
			return new UsernamePasswordCredentials(Variables.LOGSERVICE_USER, Variables.LOGSERVICE_PWD);
		} else {
			return new UsernamePasswordCredentials(Variables.WEBSERVICE_USER, Variables.WEBSERVICE_PWD);
		}
	}
	
	private static boolean netWorkNormal(){
		if (!NetWork.checkNetWorkStatus(MyApplication.getContextObject())) {
			return false;
		}
		return true;
	}
	
	public static TResult<Boolean, String> sendPostRequest3(UrlEnum url, String relativeUrl, Map<String, String> params) {
		try {
			UsernamePasswordCredentials creds = getCredentials(url);
			String absoluteUrlStr = getAbsoluteUrl(url, relativeUrl);
			URL absoluteUrl= new URL(absoluteUrlStr);
			final CloseableHttpClient httpClient = getApacheHttpClient3(absoluteUrl, creds);
			
			final HttpPost post = new HttpPost(getAbsoluteUrl(url, relativeUrl));
			List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
			Set<Entry<String, String>> entrys = null;  
	        if (params != null && !params.isEmpty()) {  
	            entrys = params.entrySet();  
	            for (Map.Entry<String, String> entry : entrys) {
	            	pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	            }  
	        }  
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
			entity.setContentType("application/x-www-form-urlencoded");
			entity.setContentEncoding("UTF-8");
            post.setEntity(entity);  
			
            ExecutorService executor = Executors.newSingleThreadExecutor();
            FutureTask<HttpResponse> futureTask = new FutureTask<HttpResponse>(
					new Callable<HttpResponse>() {// 使用Callable接口作为构造参数
						public HttpResponse call() {
							HttpResponse response = null;
							try {
								response = httpClient.execute(post);
							} catch (Exception e) {
							}
							return response;
						}
					});
			executor.execute(futureTask);
            
			HttpResponse response = null;
			try {
				response = futureTask.get(900 * 1000, TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				futureTask.cancel(true);
			} finally {
				executor.shutdown();
			}
			if(response != null) {
				if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatusLine().getStatusCode());
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(response.getEntity().getContent())));
				String output;
				StringBuilder result = new StringBuilder();
				while ((output = br.readLine()) != null) {
					result.append(output);
				}
				return new TResult<Boolean, String>(true, result.toString());
			} else {
				return new TResult<Boolean, String>(false, "网络不给力");
			}
			
		} catch (Throwable e) {
			Log.e("HttpRequestUtil", "sendPostRequest", e);
			return new TResult<Boolean, String>(false, "网络不给力(01)");
		}
	}
	
	
	protected static CloseableHttpClient getApacheHttpClient(URL targetUrl , UsernamePasswordCredentials creds){
		try {
			// Socket config
			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(60 * 1000).build();
			// Auth
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(new AuthScope(targetUrl.getHost(), targetUrl.getPort(), null),creds);
			// Build HttpClient
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			CloseableHttpClient client = httpClientBuilder.useSystemProperties().setDefaultSocketConfig(socketConfig)
					.setDefaultCredentialsProvider(credentialsProvider).build();

			return client;
		} catch (Throwable e) {
			Log.e("ApacheHttpClientHelper", "ERROR >> ", e);
			return null;
		}
	}
	
	
	protected static CloseableHttpClient getApacheHttpClient3(URL targetUrl , UsernamePasswordCredentials creds){
		try {
			// Socket config
			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(15 * 60 * 1000).build();
			// Auth
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(new AuthScope(targetUrl.getHost(), targetUrl.getPort(), null),creds);
			// Build HttpClient
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			CloseableHttpClient client = httpClientBuilder.useSystemProperties().setDefaultSocketConfig(socketConfig)
					.setDefaultCredentialsProvider(credentialsProvider).build();

			return client;
		} catch (Throwable e) {
			Log.e("ApacheHttpClientHelper", "ERROR >> ", e);
			return null;
		}
	}
	
}