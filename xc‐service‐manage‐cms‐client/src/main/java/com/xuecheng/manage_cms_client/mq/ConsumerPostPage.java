package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @Author lcy
 * @Date 2020/3/17
 * @Description 监听Mq 接受页面发布消息
 */
@Component
@Slf4j
public class ConsumerPostPage {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        Map map = JSON.parseObject(msg, Map.class);
        log.info("【Mq接受信息】 接受的信息:{}", msg.toString());
        //取出页面id
        String pageId = (String) map.get("pageId");
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            log.error("【Mq接受信息】 MQ接受的PageId不存在");
            ExceptionCast.cast(CmsCode.CMS_MQ_PAGEID_NOTEXISTS);
        }
        //将页面保存到服务器物理路径
        pageService.savePageToServerPath(pageId);

    }
}
