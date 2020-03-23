package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author lcy
 * @Date 2020/3/22
 * @Description 预览课程响应结果
 */
@Data
@ToString
@NoArgsConstructor
public class CourseView implements Serializable {
    /**
     * 基础信息
     */
    CourseBase courseBase;
    /**
     * 课程营销
     */
    CourseMarket courseMarket;
    /**
     * 课程图片
     */
    CoursePic coursePic;
    /**
     * 教学计划
     */
    TeachplanNode TeachplanNode;
}
