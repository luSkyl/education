package com.xuecheng.manage_cms.controller;

import cn.hutool.core.io.IoUtil;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * @Author lcy
 * @Date 2020/3/15
 * @Description 页面预览
 */
@Controller
public class CmsPagePreviewController extends BaseController {
    @Autowired
    private PageService pageService;


    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId")String pageId){
        String pageHtml = pageService.getPageHtml(pageId);
        try {
            IoUtil.write(response.getOutputStream(),true,pageHtml.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
