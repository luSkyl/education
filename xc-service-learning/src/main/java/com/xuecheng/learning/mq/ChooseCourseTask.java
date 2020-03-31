package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.LearningService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author lcy
 * @Date 2020/3/31
 * @Description
 */
@Component
@Slf4j
public class ChooseCourseTask {
    @Autowired
    private LearningService learningService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 接收选课任务
     */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE})
    public void receiveChoosecourseTask(XcTask xcTask) throws IOException {
        log.info("【MQ接受选课任务】,taskId:{}", xcTask.getId());
        //接收到 的消息id
        String id = xcTask.getId();
        //添加选课
        try {
            String requestBody = xcTask.getRequestBody();
            Map map = JSON.parseObject(requestBody, Map.class);
            String userId = (String) map.get("userId");
            String courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            Date startTime = null;
            Date endTime = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (map.get("startTime") != null) {
                startTime = dateFormat.parse((String) map.get("startTime"));
            }
            if (map.get("endTime") != null) {
                endTime = dateFormat.parse((String) map.get("endTime"));
            }
            //添加选课
            ResponseResult addcourse = learningService.addcourse(userId, courseId, valid, startTime, endTime, xcTask);
            //选课成功发送响应消息
            if (addcourse.isSuccess()) {
                //发送响应消息
                rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,
                        RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY, xcTask);
                log.info("【MQ发送选课完成消息】 taskId:{}", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【MQ发送选课完成消息】 MQ发送选课完成消息失败 taskId:{}, e:{}", id, e.getMessage());
        }
    }
}