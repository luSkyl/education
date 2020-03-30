package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.course.response.QueryResponseResult;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.Map;

/**
 * @Author lcy
 * @Date 2020/3/27
 * @Description
 */
@Api(value = "课程搜索",description = "课程搜索",tags = {"课程搜索"})
public interface EsCourseControllerApi {
    @ApiOperation("课程搜索")
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) throws IOException;

    /**
     * 根据id查询课程信息
     * @param id
     * @return
     * 返回的课程信息为json结构：key为课程id，value为课程内容。
     */
    @ApiOperation("根据id查询课程信息")
    public Map<String,CoursePub> getall(String id);


    /**
     * 根据课程计划查询媒资信息
     * @param teachplanId
     * @return
     */
    @ApiOperation("根据课程计划查询媒资信息")
    public TeachplanMediaPub getmedia(String teachplanId);
}