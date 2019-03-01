package com.example.processor;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * 判断是否需要消息者配置
 * .<br/>
 * 
 * Copyright: Copyright (c) 2017  zteits
 * 
 * @ClassName: MQProducerCondition
 * @Description: 
 * @version: v1.0.0
 * @author: zhaowg
 * @date: 2018年3月1日 上午11:22:56
 */
public class MQConsumerCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		//判断当前环境开关是否开启
		String isOnOff = context.getEnvironment().getProperty("rocketmq.consumer.isOnOff");
		//当且仅当值为on时，返回true
		if(!StringUtils.isEmpty(isOnOff) && isOnOff.equalsIgnoreCase("on")){
			return true;
		}
		return false;
	}

}
