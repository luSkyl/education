package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description
 */
@Mapper
public interface CategoryMapper {


    /**
     * 查询树状分类列表
     *
     * @return
     */
    CategoryNode selectCategoryList();
}
