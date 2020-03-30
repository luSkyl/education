package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author lcy
 * @Date 2020/3/28
 * @Description
 * 1、上传前检查上传环境
 * 检查文件是否上传，已上传则直接返回。
 * 检查文件上传路径是否存在，不存在则创建。
 * 2、分块检查
 * 检查分块文件是否上传，已上传则返回true。
 * 未上传则检查上传路径是否存在，不存在则创建。
 * 3、分块上传
 * 将分块文件上传到指定的路径。
 * 4、合并分块
 * 将所有分块文件合并为一个文件。
 * 在数据库记录文件信息。
 *
 */
@Api(value = "媒资管理接口",description = "媒资管理接口，提供文件上传，文件处理等接口")
public interface MediaUploadControllerApi {
    @ApiOperation("文件上传注册")
    public ResponseResult register(String fileMd5,
                                   String fileName,
                                   Long fileSize,
                                   String mimetype,
                                   String fileExt);
    @ApiOperation("分块检查")
    public CheckChunkResult checkchunk(String fileMd5,
                                       Integer chunk,
                                       Integer chunkSize);
    @ApiOperation("上传分块")
    public ResponseResult uploadchunk(MultipartFile file,
                                      Integer chunk,
                                      String fileMd5);
    @ApiOperation("合并文件")
    public ResponseResult mergechunks(String fileMd5,
                                      String fileName,
                                      Long fileSize,
                                      String mimetype,
                                      String fileExt);
}