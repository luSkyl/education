package com.xuecheng.framework.domain.cms.ext;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import lombok.Data;
import lombok.ToString;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@Data
@ToString
public class CmsTemplateExt extends CmsTemplate {

    //模版内容
    private String templateValue;

}
