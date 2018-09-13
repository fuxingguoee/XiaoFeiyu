/**
 * 
 */
package com.il360.xiaofeiyu.util.alipay;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Administrator
 *
 */
public class HttpResult
{
	public HttpResult(HttpResponse resp)
	{
		this(resp, null);
	}

	public HttpResult(HttpResponse resp, HttpUriRequest req)
	{
		this(resp, req, Charset.defaultCharset());
	}

	public HttpResult(HttpResponse resp, HttpUriRequest req, Charset charset)
	{
		this.response = resp;
		try
		{
			this.html = EntityUtils.toString(response.getEntity(), charset.name());
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.request = req;
	}

	private HttpResponse response;
	private String html;
	private HttpUriRequest request;

	public HttpResponse getResponse()
	{
		return response;
	}

	public void setResponse(HttpResponse response)
	{
		this.response = response;
	}

	public String getHtml()
	{
		return html;
	}

	public void setHtml(String html)
	{
		this.html = html;
	}

	public HttpUriRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpUriRequest request)
	{
		this.request = request;
	}

//	public JSONObject getJSON()
//	{
//		try
//		{
//			return JSONObject.fromObject(html);
//		}
//		catch (Exception e)
//		{
//			// e.printStackTrace();
//			return new JSONObject();
//		}
//	}
}
