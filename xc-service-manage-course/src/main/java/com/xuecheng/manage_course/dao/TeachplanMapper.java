package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author lcy
 * @Date 2020/3/18
 * @Description
 */
@Mapper
public interface TeachplanMapper {
     /**
      * 根据课程Id查询
      * @param courseId
      * @return
      */
     TeachplanNode selectList(String courseId);
}

