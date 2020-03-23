package com.xuecheng.framework.domain.course.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 查询课程的条件
 */
@Data
@ToString
public class CourseListRequest extends RequestData {
    /**
     * 公司Id
     */
    private String companyId;
}
