package com.example.except;

import com.example.constant.ErrorCode;

/**
 * 消息异常
 * .<br/>
 * 
 * Copyright: Copyright (c) 2017  zteits
 * 
 * @ClassName: RocketMQException
 * @Description: 
 * @version: v1.0.0
 * @author: zhaowg
 * @date: 2018年3月2日 下午11:01:41
 * Modification History:
 * Date             Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月2日      zhaowg           v1.0.0               创建
 */
public class RocketMQException extends AppException{

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 无参构造函数
	 */
	public RocketMQException() {
		super();
	}
	public RocketMQException(Throwable e) {
		super(e);
	}
	public RocketMQException(ErrorCode errorType) {
		super(errorType);
	}
	
	public RocketMQException(ErrorCode errorCode, String ... errMsg) {
		super(errorCode, errMsg);
	}
	/**
	 * 封装异常
	 * @param errorCode
	 * @param errMsg
	 * @param isTransfer 是否转换异常信息，如果为false,则直接使用errMsg信息
	 */
	public RocketMQException(ErrorCode errorCode, String errMsg, Boolean isTransfer) {
		super(errorCode, errMsg,isTransfer);
	}
	
	public RocketMQException(ErrorCode errCode, Throwable cause,String ... errMsg) {
		super(errCode,cause, errMsg);
	}
}
