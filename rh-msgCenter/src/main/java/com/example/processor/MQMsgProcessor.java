package com.example.processor;

import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消息队列-消息消费处理接口
 * .<br/>
 * 
 * Copyright: Copyright (c) 2017  zteits
 * 
 * @ClassName: MQMsgProcessorService
 * @Description: 
 * @version: v1.0.0
 * @author: zhaowg
 * @date: 2018年3月1日 上午9:57:57
 */
public interface MQMsgProcessor {
	/**
	 * 消息处理<br/>
	 * 如果没有return true ，consumer会重新消费该消息，直到return true<br/>
	 * consumer可能重复消费该消息，请在业务端自己做是否重复调用处理，该接口设计为幂等接口
	 * @param topic 消息主题
	 * @param tag 消息标签
	 * @param msgs 消息
	 * @return
	 * 2018年3月1日 zhaowg
	 */
	MQConsumeResult handle(String topic, String tag, List<MessageExt> msgs);
}
