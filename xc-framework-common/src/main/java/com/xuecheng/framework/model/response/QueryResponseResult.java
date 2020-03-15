package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */

@Data
@ToString
public class QueryResponseResult extends ResponseResult {

    QueryResult queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

}
