package com.xuecheng.framework.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@Data
@ToString
public class QueryResult<T> {
    /**
     * 数据列表
     */
    private List<T> list;
    /**
     * 数据总数
     */
    private long total;
}
