package com.barcode.mobile.common;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class CustomerHttpClient {

	private static final String FILE_UPLOAD_NAME = "files";
	private static final String TAG = "CustomerHttpClient";
	private static final String CHARSET = HTTP.UTF_8;
//	private static final String CHARSET = "GBK";//终端直供只能用GBK
	private static HttpClient customerHttpClient;

	private CustomerHttpClient() {
	}

	public static synchronized HttpClient getHttpClient() {
		if (null == customerHttpClient) {
			customerHttpClient = createClient();
		}
		return customerHttpClient;
	}
	
	public static void resetClient(){
		customerHttpClient = createClient();
	}
	
	private static synchronized HttpClient createClient(){
		HttpParams params = new BasicHttpParams();
		// 设置一些基本参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpProtocolParams
				.setUserAgent(
						params,
						"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
								+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
		// 超时设置
		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout(params, 10000);
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, 20000);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, 20000);

		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		return new DefaultHttpClient(conMgr, params);
	}
	
	/**
	 * 一般的提交请求参数
	 * @param url
	 * @param params
	 * @return
	 * @throws ConnectionPoolTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 */
	public static String post(String url, List<NameValuePair> params) throws ConnectionPoolTimeoutException,ConnectTimeoutException,SocketTimeoutException {
        try {
            // 编码参数
        	List<NameValuePair> formparams=params;
        	if(null==formparams){
        		formparams=new ArrayList<NameValuePair>(); // 请求参数
        	}
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, CHARSET);
            // 创建POST请求
            HttpPost request = new HttpPost(url);
            request.setEntity(entity);
            // 发送请求
            HttpClient client = getHttpClient();
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity resEntity =  response.getEntity();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, e.getMessage());
            return null;
        } catch (ClientProtocolException e) {
            Log.w(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            throw new RuntimeException("连接失败", e);
        }
    }
	
	/**
	 * 上传文件
	 * @param url
	 * @param params
	 * @param filePath
	 * @return
	 * @throws ConnectionPoolTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 */
	public static String postUploadFile(String url, List<NameValuePair> params, String filePath) throws ConnectionPoolTimeoutException,ConnectTimeoutException,SocketTimeoutException {
		try {
			MultipartEntity mpEntity=new MultipartEntity();
			//一般的请求参数
			for (NameValuePair param : params) {
				mpEntity.addPart(param.getName(), new StringBody(param.getValue()));
			}
			//文件
			mpEntity.addPart(FILE_UPLOAD_NAME, new FileBody(new File(filePath)));
			// 创建POST请求
			HttpPost request = new HttpPost(url);
			request.setEntity(mpEntity);
			// 发送请求
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			    throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity =  response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(TAG, e.getMessage());
            return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(TAG, e.getMessage());
            return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(TAG, e.getMessage());
            return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("连接失败", e);
		}
	}
}
