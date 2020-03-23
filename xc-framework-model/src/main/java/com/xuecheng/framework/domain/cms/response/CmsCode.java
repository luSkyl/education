package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 错误代码
 */
@ToString
public enum CmsCode implements ResultCode {
    /**
     * 页面名称已存在
     */
    CMS_ADDPAGE_EXISTSNAME(false, 24001, "页面名称已存在！"),
    /**
     * 从页面信息中找不到获取数据的url
     */
    CMS_GENERATEHTML_DATAURLISNULL(false, 24002, "从页面信息中找不到获取数据的url！"),
    /**
     * 根据页面的数据url获取不到数据
     */
    CMS_GENERATEHTML_DATAISNULL(false, 24003, "根据页面的数据url获取不到数据！"),
    /**
     * 页面模板为空
     */
    CMS_GENERATEHTML_TEMPLATEISNULL(false, 24004, "页面模板为空！"),
    /**
     * 生成的静态html为空
     */
    CMS_GENERATEHTML_HTMLISNULL(false, 24005, "生成的静态html为空！"),
    /**
     * 保存静态html出错
     */
    CMS_GENERATEHTML_SAVEHTMLERROR(false, 24006, "保存静态html出错！"),
    /**
     * 预览页面为空
     */
    CMS_COURSE_PERVIEWISNULL(false, 24007, "预览页面为空！"),
    /**
     * 页面不存在
     */
    CMS_PAGE_NOTEXISTS(false,24008,"页面不存在!"),
    /**
     * CMS数据模型信息不存在
     */
    CMS_CONFIG_NOTEXISTS(false,24009,"CMS数据模型信息不存在!"),
    /**
     * 页面模板不存在
     */
    CMS_GENERATEHTML_TEMPLATENOTEXISTS(false,24010,"页面模板不存在!"),
    /**
     * CMS站点不存在
     */
    CMS_SITE_ISNULL(false,24011,"CMS站点不存在!"),
    /**
     * 静态文件Id为空
     */
    CMS_HTMLFILEID_ISNULL(false,24011,"CMS站点不存在!"),
    /**
     * MQ接受的PageId不存在
     */
    CMS_MQ_PAGEID_NOTEXISTS(false,24011,"MQ接受的PageId不存在!"),
    /**
     * 页面物理路径为空
     */
    CMS_PAGEPHYSICALPATH_ISNULL(false,24012,"页面物理路径为空!"),
    ;


    /**
     * 操作代码
     */
    boolean success;
    /**
     * 操作代码
     */
    int code;
    /**
     * 提示信息
     */
    String message;

    private CmsCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
