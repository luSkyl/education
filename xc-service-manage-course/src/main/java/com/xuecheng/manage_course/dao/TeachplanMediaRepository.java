package com.xuecheng.manage_course.dao;


import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/29
 * @Description
 */
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia,String> {
    /**
     * 从TeachplanMedia查询课程计划媒资信息
     * @param courseId
     * @return
     */
    List<TeachplanMedia> findByCourseId(String courseId);

}
