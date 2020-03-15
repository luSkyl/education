package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lcy
 * @Date 2020/3/14
 * @Description
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
    /**
     * 根据SiteId 查找 SiteName
     * @param siteId
     * @return
     */
    CmsSite findBySiteId(String siteId);
}
