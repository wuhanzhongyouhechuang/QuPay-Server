package com.example.constant;
/**
 * 消息发送状态：-201：定时任务发送失败（人工处理），-200：生产者发送失败（定时任务处理），100：待发送，200：生产者发送成功，201：定时任务发送成功
 * .<br/>
 * 
 * Copyright: Copyright (c) 2017  zteits
 * 
 * @ClassName: ProductSendState
 * @Description: 
 * @version: v1.0.0
 * @author: zhaowg
 * @date: 2018年2月28日 下午4:02:24
 * Modification History:
 * Date             Author          Version            Description
 *---------------------------------------------------------*
 * 2018年2月28日      zhaowg           v1.0.0               创建
 */
public enum ProductSendStateEnum {
	/**消息发送状态：-201：定时任务发送失败（人工处理），-200：生产者发送失败（定时任务处理），100：待发送，200：生产者发送成功，201：定时任务发送成功*/
	
	JOB_SEND_FAIL(-201),
	PRODUCT_SEND_FAIL(-200),
	WAIT_SEND(100),
	PRODUCT_SEND_SUCCESS(200),
	JOB_SEND_SUCCESS(201),
	;
	private int code;

    private ProductSendStateEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
