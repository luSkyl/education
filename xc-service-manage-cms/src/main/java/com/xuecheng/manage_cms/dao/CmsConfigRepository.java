package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lcy
 * @Date 2020/3/15
 * @Description
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {

}
