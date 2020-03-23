package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description
 */
@Data
public class QueryResponseResult<E> extends ResponseResult {
    private QueryResult<E> queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult<E> queryResult) {
        super(resultCode);
        this.queryResult = queryResult;
    }


}
