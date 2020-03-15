package com.xuecheng.framework.domain.cms;

import lombok.Data;

import java.util.Map;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 数据模型项目
 */
@Data
public class CmsConfigModel {
    /**
     * 主键
     */
    private String key;
    /**
     * 项目名
     */
    private String name;
    /**
     * 项目url
     */
    private String url;
    /**
     * 项目复杂值
     */
    private Map mapValue;
    /**
     * 项目简单值
     */
    private String value;

}
