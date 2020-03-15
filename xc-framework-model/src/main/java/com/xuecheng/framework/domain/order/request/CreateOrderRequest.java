package com.xuecheng.framework.domain.order.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@Data
@ToString
public class CreateOrderRequest extends RequestData {

    String courseId;

}
