package com.barcode.mobile.common;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;

public interface AsyncTaskCallBack {

	public String process(CommAsyncTask task, String serviceUrl,
			PageInfo newPage, String... params)
			throws ConnectionPoolTimeoutException, ConnectTimeoutException,
			SocketTimeoutException, Exception;
}
