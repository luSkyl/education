package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.course.SysDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDicthinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description
 */
@RestController
@RequestMapping("/sys/dictionary/")
public class SysDicthinaryController implements SysDicthinaryControllerApi {

    @Autowired
    private SysDicthinaryService sysDicthinaryService;

    @GetMapping("get/{type}")
    @Override
    public SysDictionary getByType(@PathVariable("type") String type) {
        return sysDicthinaryService.get(type);
    }
}
