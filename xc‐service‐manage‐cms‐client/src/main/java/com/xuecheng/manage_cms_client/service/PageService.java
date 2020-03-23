package com.xuecheng.manage_cms_client.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * @Author lcy
 * @Date 2020/3/16
 * @Description
 */
@Service
@Slf4j
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    private static volatile StrBuilder strBuilder;

    static {
        strBuilder = new StrBuilder();
    }


    /**
     * 将页面html保存到页面物理路径
     *
     * @param pageId
     */
    public void savePageToServerPath(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面物理路径
        CmsPage cmsPage = optional.get();
        //页面物理路径
        String pagePath = strBuilder.append(cmsPage.getPagePhysicalPath())
                .append(cmsPage.getPageName()).toStringAndReset();
        //查询页面静态文件
        try (InputStream inputStream = this.getFileById(cmsPage.getHtmlFileId()); FileOutputStream outputStream = new FileOutputStream(pagePath)) {
            IoUtil.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件id获取文件内容
     *
     * @param fileId
     * @return
     */
    public InputStream getFileById(String fileId) {
        //定义文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //定义GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        InputStream inputStream = null;
        try {
            inputStream = gridFsResource.getInputStream();
        } catch (IOException e) {
            log.error("【根据文件id获取文件内容】根据文件id获取文件内容失败 e:{}", CmsCode.CMS_GENERATEHTML_HTMLISNULL.message());
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return inputStream;
    }
}
