package com.xuecheng.framework.domain.cms;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 站点id、服务器IP、端口、访问地址、服务器类型（代理、静态、动态、CDN）、资源发布地址（完整的HTTP接口）、使用类型（测试、生产）
 */
@Data
@ToString
@Document(collection = "cms_template")
public class CmsTemplate {

    /**
     * 站点ID
     */
    private String siteId;
    /**
     * 模版ID
     */
    @Id
    private String templateId;
    /**
     * 模版名称
     */
    private String templateName;
    /**
     * 模版参数
     */
    private String templateParameter;
    /**
     * 模版文件Id
     */
    private String templateFileId;
}
