package com.xuecheng.manage_cms.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.PageUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import sun.net.www.content.image.gif;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private CmsConfigRepository cmsConfigRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 页面列表分页查询
     *
     * @param page             当前页码
     * @param size             页面显示个数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     * 站点Id：精确匹配
     * 模板Id：精确匹配
     * 页面别名：模糊匹配
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //条件匹配器
        //页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        //条件值
        CmsPage cmsPage = new CmsPage();
        //站点ID
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //页面别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        if (page <= 0) {
            page = 1;
        }
        //为了适应mongodb的接口将页码减1
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }

        PageRequest pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult queryResult = new QueryResult();
        //数据列表
        queryResult.setList(all.getContent());
        //总记录数
        queryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /**
     * Site下拉选
     *
     * @return
     */
    public QueryResponseResult findSiteList() {
        List<CmsPage> pageList = cmsPageRepository.findAll();
        if (CollectionUtil.isNotEmpty(pageList)) {
            Set<CmsSite> cmsSiteSet = Sets.newHashSet();
            pageList.stream().map(e -> e.getSiteId()).filter(e -> StringUtils.isNotBlank(e)).forEach(e -> {
                if (!cmsSiteSet.contains(e)) {
                    CmsSite cmsSite = new CmsSite();
                    cmsSite.setSiteId(e);
                    cmsSite.setSiteName(cmsSiteRepository.findBySiteId(e).getSiteName());
                    cmsSiteSet.add(cmsSite);
                }
            });
            QueryResult queryResult = new QueryResult();
            List<CmsSite> cmsSiteList = new ArrayList<>(cmsSiteSet);
            if (CollectionUtil.isNotEmpty(cmsSiteList)) {
                queryResult.setList(cmsSiteList);
                queryResult.setTotal(cmsSiteList.size());
                return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
            }
        }
        return new QueryResponseResult(CommonCode.FAIL, null);
    }

    /**
     * template下拉选
     *
     * @return
     */
    public QueryResponseResult findTemplateList() {
        List<CmsPage> pageList = cmsPageRepository.findAll();
        if (CollectionUtil.isNotEmpty(pageList)) {
            Set<CmsTemplate> cmsTemplateSet = Sets.newHashSet();
            pageList.stream().map(e -> e.getTemplateId()).filter(e -> StringUtils.isNotBlank(e)).forEach(e -> {
                if (!cmsTemplateSet.contains(e)) {
                    CmsTemplate cmsTemplate = new CmsTemplate();
                    cmsTemplate.setTemplateId(e);
                    cmsTemplate.setTemplateName(cmsTemplateRepository.findByTemplateId(e).getTemplateName());
                    cmsTemplateSet.add(cmsTemplate);
                }
            });
            QueryResult queryResult = new QueryResult();
            List<CmsTemplate> cmsTemplateList = new ArrayList<>(cmsTemplateSet);
            if (CollectionUtil.isNotEmpty(cmsTemplateList)) {
                queryResult.setList(cmsTemplateList);
                queryResult.setTotal(cmsTemplateList.size());
                return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
            }
        }
        return new QueryResponseResult(CommonCode.FAIL, null);
    }


    /**
     * 添加页面
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            log.info("【添加页面】 添加页面失败 添加页面为空 e:{}", CmsCode.CMS_PAGE_NOTEXISTS.message());
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        Optional<CmsPage> page = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (page.isPresent()) {
            //页面已存在
            log.info("【添加页面】 添加页面失败 页面已存在 e:{}", CmsCode.CMS_ADDPAGE_EXISTSNAME.message());
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    /**
     * 根据id查询页面
     *
     * @param id
     * @return
     */
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (!optional.isPresent()) {
            log.info("【根据id查询页面】 根据id查询页面失败 e:{}", CmsCode.CMS_PAGE_NOTEXISTS.message());
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //返回空
        return optional.get();
    }

    /**
     * 更新页面信息
     *
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //根据id查询页面信息
        CmsPage one = this.getById(id);
        if (one != null) {
            BeanUtil.copyProperties(cmsPage, one);
            //执行更新
            CmsPage save = cmsPageRepository.save(one);
            if (save != null) {
                //返回成功
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }
        }
        //返回失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }


    /**
     * 根据Id删除页面
     *
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        CmsPage one = this.getById(id);
        if (one != null) {
            //删除页面
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 根据id查询配置管理信息
     *
     * @param id
     * @return
     */
    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (!optional.isPresent()) {
            log.info("【根据id查询配置管理信息】 根据id查询配置管理信息失败 e:{}", CmsCode.CMS_CONFIG_NOTEXISTS.message());
            ExceptionCast.cast(CmsCode.CMS_CONFIG_NOTEXISTS);
        }
        return optional.get();
    }

    /**
     * 页面静态化
     *
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId) {
        //获取页面模型数据
        Map model = getModelByPageId(pageId);
        ////获取页面模板
        String templateContent = getTemplateByPageId(pageId);
        //执行静态化
        return generateHtml(templateContent, model);
    }


    /**
     * 页面静态化
     *
     * @param template
     * @param model
     * @return
     */
    private String generateHtml(String template, Map model) {
        String html = StringUtils.EMPTY;
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", template);
            //配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template template1 = configuration.getTemplate("template");
            html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(html)) {
            log.info("【页面静态化】 页面静态化失败 e:{}", CmsCode.CMS_GENERATEHTML_HTMLISNULL.message());
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    /**
     * 获取页面模板
     *
     * @param pageId
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        //查询页面信息
        CmsPage cmsPage = this.getById(pageId);
        //页面模板
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isBlank(templateId)) {
            log.info("【获取页面模板】 获取页面模板失败 e:{}", CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL.message());
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> cmsTemplate = cmsTemplateRepository.findById(templateId);
        if (!cmsTemplate.isPresent()) {
            log.info("【获取页面模板】 获取页面模板失败 e:{}", CmsCode.CMS_GENERATEHTML_TEMPLATENOTEXISTS.message());
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATENOTEXISTS);
        }
        //模板文件id
        String templateFileId = cmsTemplate.get().getTemplateFileId();
        //取出模板文件内容
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        String content = StringUtils.EMPTY;
        try {
            content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(content)) {
            log.info("【获取页面模板】 获取页面模板失败 e:{}", CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL.message());
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        return content;
    }


    /**
     * 获取页面模型数据
     *
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isBlank(dataUrl)) {
            log.info("【获取页面模型数据】 获取页面模型数据失败，e:{}", CmsCode.CMS_GENERATEHTML_DATAURLISNULL.message());
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //从dataUrl中取出数据，然后放入Map中
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map map = forEntity.getBody();
        if (CollectionUtil.isEmpty(map)) {
            log.info("【获取页面模型数据】 获取页面模型数据失败，e:{}", CmsCode.CMS_GENERATEHTML_DATAISNULL.message());
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        return map;
    }


}
