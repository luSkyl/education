package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author lcy
 * @Date 2020/3/31
 * @Description
 */
@Service
@Slf4j
public class TaskService {
    @Autowired
    private XcTaskRepository xcTaskRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;

    /**
     * 取出前n条任务,取出指定时间之前处理的任务
     *
     * @param updateTime
     * @param n
     * @return
     */
    public List<XcTask> findTaskList(Date updateTime, int n) {
        //设置分页参数，取出前n 条记录
        Pageable pageable = PageRequest.of(0, n);
        Page<XcTask> xcTasks = xcTaskRepository.findByUpdateTimeBefore(pageable, updateTime);
        return xcTasks.getContent();
    }

    /**
     * //发送消息
     * @param xcTask 任务对象
     * @param ex 交换机id
     * @param routingKey
     */
    @Transactional(rollbackFor = Exception.class)
    public void publish(XcTask xcTask,String ex,String routingKey){
        //查询任务
        Optional<XcTask> taskOptional = xcTaskRepository.findById(xcTask.getId());
        if(taskOptional.isPresent()){
            XcTask one = taskOptional.get();
            //String exchange, String routingKey, Object object
            rabbitTemplate.convertAndSend(ex,routingKey,xcTask);
            //更新任务时间为当前时间
            one.setUpdateTime(new Date());
            xcTaskRepository.save(one);
        }
    }

    /**
     * 获取任务(采用乐观锁机制 防止多线程错误)
     * @param taskId
     * @param version
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int getTask(String taskId,int version){
        int i = xcTaskRepository.updateTaskVersion(taskId, version);
        return i;
    }


    /**
     * 删除任务
     * 订单服务接收MQ完成选课的消息，将任务从当前任务表删除，将完成的任务添加到完成任务表。
     * @param taskId
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void finishTask(String taskId){
        Optional<XcTask> taskOptional = xcTaskRepository.findById(taskId);
        if(!taskOptional.isPresent()){
            log.error("【删除任务】 删除任务失败");
           ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        XcTask xcTask = taskOptional.get();
        xcTask.setDeleteTime(new Date());
        XcTaskHis xcTaskHis = new XcTaskHis();
        BeanUtils.copyProperties(xcTask, xcTaskHis);
        xcTaskHisRepository.save(xcTaskHis);
        xcTaskRepository.delete(xcTask);
    }



}