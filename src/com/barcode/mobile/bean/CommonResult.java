package com.barcode.mobile.bean;

/**
 * 返回结果
 * 
 * @author laien
 * 
 */
public class CommonResult {
	// 是否成功
	private boolean success;
	// 失败信息
	private String errorMessage;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
