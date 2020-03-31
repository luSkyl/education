package com.xuecheng.order.task;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/31
 * @Description 接收到添加选课的消息调用添加选课方法完成添加选课，并发送完成选课消息
 */
@Component
@Slf4j
public class ChooseCourseTask {
    @Autowired
    private TaskService taskService;

    /**
     * 每隔1分钟扫描消息表，向mq发送消息
     */
    @Scheduled(fixedDelay = 60000)
    public void sendChoosecourseTask() {
        //取出当前时间1分钟之前的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE, -1);
        Date time = calendar.getTime();
        List<XcTask> taskList = taskService.findTaskList(time, 1000);
        //遍历任务列表
        for (XcTask xcTask : taskList) {
            if (taskService.getTask(xcTask.getId(), xcTask.getVersion()) > 0) {
                //发送选课消息
                taskService.publish(xcTask, xcTask.getMqExchange(), xcTask.getMqRoutingkey());
                log.info("【MQ发送选课消息】 id:{}", xcTask.getId());
            }
        }
    }


    /**
     * 接收选课响应结果
     */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE})
    public void receiveFinishChoosecourseTask(XcTask task) throws IOException {
        log.info("【MQ接受选课完成信息】 TaskId:{}", task.getId());
        //接收到 的消息id
        String id = task.getId();
        if (task == null || StringUtils.isBlank(id)) {
            log.error("【MQ接受选课完成信息】 MQ接受选课完成信息失败");
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //删除任务，添加历史任务
        taskService.finishTask(id);
    }
}
