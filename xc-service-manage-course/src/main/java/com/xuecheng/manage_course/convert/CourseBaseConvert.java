package com.xuecheng.manage_course.convert;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description CourseBase转换类
 */
@Mapper(componentModel = "spring")
public interface CourseBaseConvert {
    CourseBaseConvert INSTANCE = Mappers.getMapper(CourseBaseConvert.class);

    /**
     * 属性赋值
     * @param courseBase
     * @return
     */
   CourseBase courseBaseConvertoCourseBase(CourseBase courseBase);
}
