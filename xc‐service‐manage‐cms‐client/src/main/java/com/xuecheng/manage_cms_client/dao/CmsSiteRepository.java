package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lcy
 * @Date 2020/3/16
 * @Description 获取站点物理路径
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
