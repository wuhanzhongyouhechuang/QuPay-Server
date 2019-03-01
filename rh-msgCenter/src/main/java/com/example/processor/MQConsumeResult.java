package com.example.processor;

import java.io.Serializable;

/**
 * 消费结果
 * .<br/>
 * 
 * Copyright: Copyright (c) 2017  zteits
 * 
 * @ClassName: MQConsumeResult
 * @Description: 
 * @version: v1.0.0
 * @author: zhaowg
 * @date: 2018年3月1日 上午11:12:55
 * Modification History:
 * Date             Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月1日      zhaowg           v1.0.0               创建
 */
public class MQConsumeResult implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 是否处理成功
	 */
	private boolean isSuccess;
	/**
	 * 如果处理失败，是否允许消息队列继续调用，直到处理成功，默认true
	 */
	private boolean isReconsumeLater = true;
	/**
	 * 是否需要记录消费日志，默认不记录
	 */
	private boolean isSaveConsumeLog = false;
	/**
	 * 错误Code
	 */
	private String errCode;
	/**
	 * 错误消息
	 */
	private String errMsg;
	/**
	 * 错误堆栈
	 */
	private Throwable e;
	/**
	 * 设置错误信息
	 * @param errCode
	 * @param errMsg
	 * @param e
	 * 2018年3月1日 zhaowg
	 */
	public void setErrInfo(String errCode,String errMsg,Throwable e){
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.e = e;
		this.isSuccess = false;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public Throwable getE() {
		return e;
	}
	public void setE(Throwable e) {
		this.e = e;
	}
	public boolean isReconsumeLater() {
		return isReconsumeLater;
	}
	public void setReconsumeLater(boolean isReconsumeLater) {
		this.isReconsumeLater = isReconsumeLater;
	}
	public boolean isSaveConsumeLog() {
		return isSaveConsumeLog;
	}
	public void setSaveConsumeLog(boolean isSaveConsumeLog) {
		this.isSaveConsumeLog = isSaveConsumeLog;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	
}
