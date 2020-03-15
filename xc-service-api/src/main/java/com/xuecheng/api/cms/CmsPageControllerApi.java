package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 页面查询接口
 */
@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    /**
     * 分页查询页面列表
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
    @ApiImplicitParam(name="page",value = "页码", required=true,paramType="path",dataType="int"),
    @ApiImplicitParam(name="size",value="每页记录数",required=true,paramType="path",dataType="int")
                    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) ;


    /**
     * 新增页面
     * @param cmsPage
     * @return
     */
    @ApiOperation("添加页面")
    public CmsPageResult add(CmsPage cmsPage);

    /**
     * 页面站点下拉选值
     * @return
     */
    @ApiOperation("页面下拉选值")
    public QueryResponseResult findSiteList();

    /**
     * 新增页面模板名下拉选值
     * @return
     */
    @ApiOperation("新增页面模板名下拉选值")
    public QueryResponseResult findTemplateList();

    /**
     * 根据id查询页面
     * @param id
     * @return
     */
    @ApiOperation("通过ID查询页面")
    public CmsPage findById(String id);


    /**
     * 保存页面信息
     * @param id
     * @param cmsPage
     * @return
     */
    @ApiOperation("修改页面")
    public CmsPageResult edit(String id,CmsPage cmsPage);

    /**
     * 通过ID删除页面
     * @param id
     * @return
     */
    @ApiOperation("通过ID删除页面")
    public ResponseResult delete(String id);




}
