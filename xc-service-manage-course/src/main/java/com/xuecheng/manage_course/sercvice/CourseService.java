package com.xuecheng.manage_course.sercvice;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.QueryResponseResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import com.xuecheng.manage_course.convert.CourseBaseConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author lcy
 * @Date 2020/3/18
 * @Description
 */
@Service
@Slf4j
public class CourseService {
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanRepository teachplanRepository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private CoursePubRepository coursePubRepository;
    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    private TeachplanMediaPubRepository teachplanMediaPubRepository;


    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;


    /**
     * 查询课程计划
     *
     * @param courseId
     * @return
     */
    public TeachplanNode findTeachplanList(String courseId) {
        if (StringUtils.isBlank(courseId)) {
            log.error("【查询课程计划】 查询课程计划失败 e:{}", CourseCode.COURSE_PUBLISH_COURSEIDISNULL.message());
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        if (teachplanNode == null) {
            log.error("【查询课程计划】 查询课程计划失败 e:{}", CourseCode.COURSE_TEACHPLANNODE_ISNULL.message());
            ExceptionCast.cast(CourseCode.COURSE_TEACHPLANNODE_ISNULL);
        }
        return teachplanNode;

    }

    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //校验课程id和课程计划名称
        if (teachplan == null || StringUtils.isBlank(teachplan.getCourseid()) || StringUtils.isBlank(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //取出课程id
        String courseid = teachplan.getCourseid();
        //取出父结点id
        String parentid = teachplan.getParentid();
        if (StringUtils.isBlank(parentid)) {
            parentid = getTeachplanRoot(courseid);
        }
        //取出父结点信息
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(parentid);
        if (!teachplanOptional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //父结点
        Teachplan teachplanParent = teachplanOptional.get();
        //父结点级别
        String parentGrade = teachplanParent.getGrade();
        //设置父结点
        teachplan.setParentid(parentid);
        //未发布
        teachplan.setStatus("0");
        //子结点的级别，根据父结点来判断
        if (parentGrade.equals("1")) {
            teachplan.setGrade("2");
        } else if (parentGrade.equals("2")) {
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(teachplanParent.getCourseid());
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    /**
     * 获取课程根结点，如果没有则添加根结点
     *
     * @param courseId
     * @return
     */
    public String getTeachplanRoot(String courseId) {
        //校验课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            log.error("【获取课程根结点】 获取课程根结点失败 e:{}", CourseCode.COURSE_PUBLISH_COURSEIDNOTEXISTS.message());
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDNOTEXISTS);
        }
        //取出课程计划根结点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (CollectionUtils.isEmpty(teachplanList)) {
            //新增一个根结点
            Teachplan teachplan = new Teachplan();
            teachplan.setGrade("1");
            teachplan.setParentid("0");
            teachplan.setPname(optional.get().getName());
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        return teachplanList.get(0).getId();
    }


    /**
     * 课程分页查询
     *
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    public QueryResponseResult<CourseInfo> findCourseList(String companyId,int page, int size, CourseListRequest courseListRequest) {
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10;
        }
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        //企业id
        courseListRequest.setCompanyId(companyId);
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        List<CourseInfo> courseInfoList = courseListPage.getResult();
        if (CollectionUtils.isEmpty(courseInfoList)) {
            log.error("【课程分页查询】 课程分页查询失败 e:{}", CourseCode.COURSE_COURSELISTREQUEST_ISNULL.message());
            ExceptionCast.cast(CourseCode.COURSE_COURSELISTREQUEST_ISNULL);
        }
        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<>();
        courseInfoQueryResult.setList(courseInfoList);
        courseInfoQueryResult.setTotal(courseInfoList.size());
        return new QueryResponseResult<>(CommonCode.SUCCESS, courseInfoQueryResult);
    }

    /**
     * 获取课程的基本信息
     *
     * @param courseId
     * @return
     */
    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> courseBase = courseBaseRepository.findById(courseId);
        if (!courseBase.isPresent()) {
            log.error("【获取课程的基本信息】 获取课程的基本信息失败 e:{}", CommonCode.INVALID_PARAM.message());
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        return courseBase.get();
    }

    /**
     * 更新课程信息
     *
     * @param id
     * @param courseBase
     * @return
     */
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(id);
        if (optionalCourseBase.isPresent()) {
            CourseBase info = optionalCourseBase.get();
            info.setName(courseBase.getName());
            info.setUsers(courseBase.getUsers());
            info.setDescription(courseBase.getDescription());
            info.setMt(courseBase.getMt());
            info.setGrade(courseBase.getGrade());
            info.setStudymodel(courseBase.getStudymodel());
            info.setTeachmode(courseBase.getTeachmode());
            courseBaseRepository.save(info);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 添加课程图片
     *
     * @param courseId
     * @param pic
     * @return
     */
    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (picOptional.isPresent()) {
            coursePic = picOptional.get();
        }
        //没有课程图片则新建对象
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        //保存课程图片
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic findCoursepic(String courseId) {
        Optional<CoursePic> coursePic = coursePicRepository.findById(courseId);
        if (!coursePic.isPresent()) {
            return null;
        }
        return coursePic.get();
    }

    /**
     * 删除课程图片
     *
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCoursePic(String courseId) {
        //执行删除，返回1表示删除成功，返回0表示删除失败
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }


    /**
     * 课程视图查询
     *
     * @param id
     * @return
     */
    public CourseView getCoruseView(String id) {
        CourseView courseView = new CourseView();
        //查询课程基本信息
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            courseView.setCourseBase(courseBase);
        }
        //查询课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }
        //查询课程图片信息
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            courseView.setCoursePic(picOptional.get());
        }
        //查询课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    /**
     * 课程预览
     *
     * @param id
     * @return
     */
    public CoursePublishResult preview(String id) {
        CmsPage cmsPage = this.createNewCmsPage(id);
        //远程请求cms保存页面信息
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //页面id
        String pageId = cmsPageResult.getCmsPage().getPageId();
        //页面url
        String pageUrl = previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    /**
     * 课程发布
     *
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CoursePublishResult publish(String courseId) {
        //课程信息
        CmsPage cmsPage = this.createNewCmsPage(courseId);
        if (cmsPage == null) {
            log.error("【课程发布】 更新课程信息失败 e;{}", CommonCode.FAIL.message());
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //发布课程详情页面
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            log.error("【课程发布】 发布课程详情页面失败 e:{}", CommonCode.FAIL.message());
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //更新课程状态
        CourseBase courseBase = saveCoursePubState(courseId);
        if (courseBase == null) {
            log.error("【课程发布】 更新课程状态失败 e:{}", CommonCode.FAIL.message());
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //课程索引...
        //创建一个课程发布模型
        CoursePub coursePub = createNewCoursePub(courseId);
        //保存到数据库
        CoursePub saveCoursePub = saveCoursePub(courseId, coursePub);
        //课程缓存...
        //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        //保存课程计划媒资信息到待索引表
        saveTeachplanMediaPub(courseId);
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    /**
     * 创建一个课程发布模型
     *
     * @return
     */
    private CoursePub createNewCoursePub(String courseId) {
        CoursePub coursePub = new CoursePub();
        coursePub.setId(courseId);
        //基础信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if (courseBaseOptional == null) {
            CourseBase courseBase = courseBaseOptional.get();
            BeanUtil.copyProperties(courseBase, coursePub);
        }
        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            BeanUtil.copyProperties(coursePic, coursePub);
        }
        //课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(courseId);
        if (marketOptional.isPresent()) {
            CourseMarket courseMarket = marketOptional.get();
            BeanUtil.copyProperties(courseMarket, coursePub);
        }
        //课程计划
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        //将课程计划转成json
        String teachplanString = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachplanString);
        return coursePub;
    }

    /**
     * 保存发布课程到数据库
     *
     * @param courseId  课程Id
     * @param coursePub 发布课程
     */
    private CoursePub saveCoursePub(String courseId, CoursePub coursePub) {
        if (StringUtils.isBlank(courseId)) {
            log.error("【保存发布课程】 保存发布课程失败 e:{}", CourseCode.COURSE_PUBLISH_COURSEIDISNULL.message());
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CoursePub coursePubNew = null;
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(courseId);
        if (coursePubOptional.isPresent()) {
            coursePubNew = coursePubOptional.get();
        } else {
            coursePubNew = new CoursePub();
        }
        copyCoursePub(coursePub,coursePubNew);
        //设置主键
        coursePubNew.setId(courseId);
        //更新时间戳为最新时间
        coursePubNew.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;

    }

    private void copyCoursePub(CoursePub source , CoursePub target){
        if(source == null || target == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        if(StringUtils.isNotBlank(source.getId())){
            target.setId(source.getId());
        }
        if(StringUtils.isNotBlank(source.getUsers())){
            target.setUsers(source.getUsers());
        }
        if(StringUtils.isNotBlank(source.getName())){
            target.setName(source.getName());
        }
        if(StringUtils.isNotBlank(source.getMt())){
            target.setMt(source.getMt());
        }
        if(StringUtils.isNotBlank(source.getSt())){
            target.setSt(source.getSt());
        }
        if(StringUtils.isNotBlank(source.getGrade())){
            target.setGrade(source.getGrade());
        }
        if(StringUtils.isNotBlank(source.getStudymodel())){
            target.setStudymodel(source.getStudymodel());
        }
        if(StringUtils.isNotBlank(source.getTeachmode())) {
            target.setTeachmode(source.getTeachmode());
        }
        if(StringUtils.isNotBlank(source.getDescription())){
            target.setDescription(source.getDescription());
        }
        if(StringUtils.isNotBlank(source.getPic())){
            target.setPic(source.getPic());
        }
        if(source.getTimestamp()!=null){
            target.setTimestamp(source.getTimestamp());
        }
        if(StringUtils.isNotBlank(source.getCharge())){
            target.setCharge(source.getCharge());
        }
        if(StringUtils.isNotBlank(source.getValid())){
            target.setValid(source.getValid());
        }
        if(StringUtils.isNotBlank(source.getQq())){
            target.setQq(source.getQq());
        }
        if(source.getPrice()!=null){
            target.setPrice(source.getPrice());
        }
        if(source.getPrice_old()!=null){
            target.setPrice_old(source.getPrice_old());
        }
        if(StringUtils.isNotBlank(source.getExpires())){
            target.setExpires(source.getExpires());
        }
        if(StringUtils.isNotBlank(source.getTeachplan())){
            target.setTeachplan(source.getTeachplan());
        }
        if(StringUtils.isNotBlank(source.getPubTime())){
            target.setPubTime(source.getPubTime());
        }
    }

    /**
     * 创建一个新的页面
     *
     * @param id
     * @return
     */
    private CmsPage createNewCmsPage(String id) {
        CourseBase one = this.getCourseBaseById(id);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(id + ".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + id);
        return cmsPage;
    }

    /**
     * 更新课程发布状态
     *
     * @param courseId
     * @return
     */
    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = this.getCourseBaseById(courseId);
        //更新发布状态
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }

    /**
     * 保存媒资信息
     * @param teachplanMedia
     * @return
     */
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if(teachplanMedia == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();

        //查询课程计划
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_ISNULL);
        }
        Teachplan teachplan = optional.get();
        //只允许为叶子结点课程计划选择视频
        String grade = teachplan.getGrade();
        if(StringUtils.isEmpty(grade) || !"3".equals(grade)){
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        TeachplanMedia one = null;
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        if(!teachplanMediaOptional.isPresent()){
            one = new TeachplanMedia();
        }else{
            one = teachplanMediaOptional.get();
        }
        //保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    /**
     * 保存课程计划媒资信息
     * @param courseId
     */
    private void saveTeachplanMediaPub(String courseId){
        //查询课程媒资信息
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        //将课程计划媒资信息存储待索引表
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubList = Lists.newArrayList();
        for(TeachplanMedia teachplanMedia:teachplanMediaList){
            TeachplanMediaPub teachplanMediaPub =new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        teachplanMediaPubRepository.saveAll(teachplanMediaPubList);
    }
}
