package com.xuecheng.framework.domain.course.response;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@ToString
public enum CourseCode implements ResultCode {
    /**
     * 删除课程失败，只允许删除本机构的课程
     */
    COURSE_DENIED_DELETE(false,31001,"删除课程失败，只允许删除本机构的课程！"),
    /**
     * 还没有进行课程预览
     */
    COURSE_PUBLISH_PERVIEWISNULL(false,31002,"还没有进行课程预览！"),
    /**
     * 创建课程详情页面出错
     */
    COURSE_PUBLISH_CDETAILERROR(false,31003,"创建课程详情页面出错！"),
    /**
     * 课程Id为空
     */
    COURSE_PUBLISH_COURSEIDISNULL(false,31004,"课程Id为空！"),
    /**
     * 课程ID不存在
     */
    COURSE_PUBLISH_COURSEIDNOTEXISTS(false,31005,"课程ID不存在！"),
    /**
     * 发布课程视图出错
     */
    COURSE_PUBLISH_VIEWERROR(false,31006,"发布课程视图出错！"),
    /**
     * 选择的媒资文件访问地址为空
     */
    COURSE_MEDIS_URLISNULL(false,31107,"选择的媒资文件访问地址为空！"),
    /**
     * 选择的媒资文件名称为空
     */
    COURSE_MEDIS_NAMEISNULL(false,31108,"选择的媒资文件名称为空！"),
    /**
     * 课程计划为空
     */
    COURSE_TEACHPLANNODE_ISNULL(false,31109,"课程计划为空！"),
    /**
     * 查询课程列表条件为空
     */
    COURSE_COURSELISTREQUEST_ISNULL(false,31110,"查询课程列表条件为空！"),
    /**
     * 查询课程列表条件中公司Id不存在
     */
    COURSE_COURSELISTREQUEST_COMPANYIDNOTEXISTS(false,31110,"查询课程列表条件中公司Id不存在！"),
    ;

    /**
     * 操作是否成功
     */
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    /**
     * 操作代码
     */
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    /**
     * 提示信息
     */
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;
    private CourseCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, CourseCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, CourseCode> builder = ImmutableMap.builder();
        for (CourseCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
