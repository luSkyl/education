package com.xuecheng.framework.domain.cms;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 数据模型信息
 */
@Data
@Document(collection = "cms_config")
public class CmsConfig {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 数据模型的名称
     */
    private String name;
    /**
     * 数据模型项目
     */
    private List<CmsConfigModel> model;

}
