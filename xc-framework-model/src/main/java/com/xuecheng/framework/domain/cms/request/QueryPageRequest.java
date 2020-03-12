package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 接受页面查询条件
 */
public class QueryPageRequest extends RequestData {
    //站点id
    private String siteId;
    //页面ID
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模版id
    private String templateId;


}
