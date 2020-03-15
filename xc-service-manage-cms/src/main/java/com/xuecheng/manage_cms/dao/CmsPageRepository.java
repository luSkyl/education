package com.xuecheng.manage_cms.dao;

import cn.hutool.core.io.IoUtil;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    /**
     * 根据页面名称查询
     *
     * @param pageName
     * @return
     */
    Optional<CmsPage> findByPageName(String pageName);

    /**
     * 根据页面名称、站点id、页面访问路径查询
     *
     * @param pageName    页面名称
     * @param setId       站点id
     * @param pageWebPath 页面访问路径查询
     * @return
     */
    Optional<CmsPage> findByPageNameAndSiteIdAndPageWebPath(String pageName, String setId, String pageWebPath);

}
