package com.example.constant;
/**
 * 定义消息队列主题<br/>
 * 注意：MQ消息队列主题，每个主题的子主题，请到tags下面定义
 * .<br/>
 * 
 * Copyright: Copyright (c) 2017  zteits
 * 
 * @ClassName: TopicEnum
 * @Description: 
 * @version: v1.0.0
 * @author: zhaowg
 * @date: 2018年2月28日 上午11:34:00
 * Modification History:
 * Date             Author          Version            Description
 *---------------------------------------------------------*
 * 2018年2月28日      zhaowg           v1.0.0               创建
 */
public enum TopicEnum {
	DemoTopic("DemoTopic","示例主题"),
	DemoNewTopic("DemoNewTopic","示例主题新"),
	;
	
	private String code;
    private String msg;

    private TopicEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}
