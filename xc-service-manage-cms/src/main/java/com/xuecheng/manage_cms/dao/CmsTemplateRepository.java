package com.xuecheng.manage_cms.dao;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lcy
 * @Date 2020/3/14
 * @Description
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {
    /**
     * 根据templateId 查找 CmsTemplate
     * @param templateId
     * @return
     */
    CmsTemplate findByTemplateId(String templateId);
}
