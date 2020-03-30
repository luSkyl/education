package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author lcy
 * @Date 2020/3/18
 * @Description 课程管理
 */
@Api(value="课程管理接口",description = "课程管理接口，提供课程的增、删、改、查")
public interface CourseControllerApi {

    /**
     * 课程计划查询
     * @param courseId
     * @return
     */
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    /**
     * 添加课程计划
     * @param teachplan
     * @return
     */
    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    /**
     * 查询课程列表
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    @ApiOperation("查询我的课程列表")
    public QueryResponseResult<CourseInfo> findCourseList(
            int page,
            int size,
            CourseListRequest courseListRequest
    );

    /**
     * 获取课程基础信息
     * @param courseId
     * @return
     * @throws RuntimeException
     */
    @ApiOperation("获取课程基础信息")
    public CourseBase getCourseBaseById(String courseId) throws RuntimeException;

    /**
     * 更新课程基础信息
     * @param id
     * @param courseBase
     * @return
     */
    @ApiOperation("更新课程基础信息")
    public ResponseResult updateCourseBase(String id,CourseBase courseBase);

    /**
     * 添加课程图片
     * @param courseId
     * @param pic
     * @return
     */
    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId,String pic);

    /**
     * 获取课程图片
     * @param courseId
     * @return
     */
    @ApiOperation("获取课程图片")
    public CoursePic findCoursePic(String courseId);

    /**
     * 删除课程图片
     * @param courseId
     * @return
     */
    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);

    /**
     * 课程视图查询
     * @param id
     * @return
     */
    @ApiOperation("课程视图查询")
    public CourseView courseview(String id);

    /**
     * 预览课程
     * @param id
     * @return
     */
    @ApiOperation("预览课程")
    public CoursePublishResult preview(String id);

    /**
     * 发布课程
     * @param id
     * @return
     */
    @ApiOperation("发布课程")
    public CoursePublishResult publish(@PathVariable String id);

    /**
     * 保存媒资信息
     * @param teachplanMedia
     * @return
     */
    @ApiOperation("保存媒资信息")
    public ResponseResult savemedia(TeachplanMedia teachplanMedia);




}
