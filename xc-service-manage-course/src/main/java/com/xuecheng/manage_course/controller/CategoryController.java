package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.sercvice.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description
 */
@RestController
@RequestMapping("/category/")
public class CategoryController implements CategoryControllerApi {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("list")
    @Override
    public CategoryNode findList() {
        return categoryService.findList();
    }

    @GetMapping("one/{id}")
    @Override
    public Category findOne(@PathVariable("id") String id) {
        return categoryService.findOne(id);
    }





}
