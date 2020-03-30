package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author lcy
 * @Date 2020/3/28
 * @Description 处理任务类 此类负责监听视频处理队列，并进行视频处理
 * <p>
 * 处理流程
 * 1）接收视频处理消息
 * 2）判断媒体文件是否需要处理（本视频处理程序目前只接收avi视频的处理）
 * 当前只有avi文件需要处理，其它文件需要更新处理状态为“无需处理”。
 * 3）处理前初始化处理状态为“未处理”
 * 4）处理失败需要在数据库记录处理日志，及处理状态为“处理失败”
 * 5）处理成功记录处理状态为“处理成功
 */
@Component
@Slf4j
public class MediaProcessTask {
    /**
     * ffmpeg绝对路径
     */
    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpegPath;

    /**
     * 上传文件根目录
     */
    @Value("${xc-service-manage-media.video-location}")
    private String serverPath;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    private static final String MEDIA_EXTNAME_AVI = "avi";
    private static final String MEDIA_EXTNAME_MP4 = "mp4";

    /**
     * MQ 接受并解析 消息
     *
     * @param msg
     * @throws IOException
     */
    @RabbitListener(queues = "${xc-service-manage-media.mq.queue-media-video-processor}",containerFactory="customContainerFactory")
    public void receiveMediaProcessTask(String msg) throws IOException {
        //1、解析消息 得到MediaId
        Map map = JSON.parseObject(msg, Map.class);
        log.info("【MQ接受消息】 Message:{}", map);
        String mediaId = (String) map.get("mediaId");
        //2、拿MediaId 从数据库查询信息
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        if (!mediaFileOptional.isPresent()) {
            return;
        }
        MediaFile mediaFile = mediaFileOptional.get();
        //媒资文件类型
        String fileType = mediaFile.getFileType();
        //目前只处理avi文件
        if (fileType == null || !fileType.equals(MEDIA_EXTNAME_AVI)) {
            //处理状态为无需处理
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
            return;
        } else {
            //处理状态为未处理
            mediaFile.setProcessStatus("303001");
            mediaFileRepository.save(mediaFile);
        }
        //3、使用工具类将avi文件生成mp4
        String videoPath = serverPath + mediaFile.getFilePath() + mediaFile.getFileName();
        String mp4Name = mediaFile.getFileId() + "." + MEDIA_EXTNAME_MP4;
        String mp4folderPath = serverPath + mediaFile.getFilePath();
        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegPath, videoPath, mp4Name, mp4folderPath);
        String result = videoUtil.generateMp4();
        if (result == null || !result.equals("success")) {
            //操作失败写入处理日志
            //处理状态为处理失败
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //4、将mp4生成m3u8跟ts文件
        //此地址为mp4的地址
        String videoPath2 = serverPath + mediaFile.getFilePath() + mp4Name;
        String m3u8Name = mediaFile.getFileId() + ".m3u8";
        String m3u8folderPath = serverPath + mediaFile.getFilePath() + "hls/";

        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpegPath, videoPath2, m3u8Name, m3u8folderPath);
        String result2 = hlsVideoUtil.generateM3u8();

        if (result == null || !result.equals("success")) {
            //操作失败写入处理日志
            mediaFile.setProcessStatus("303003");//处理状态为处理失败
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //获取m3u8列表
        List<String> tsList = hlsVideoUtil.getTsList();

        //更新处理状态为成功
        //处理状态为处理成功
        mediaFile.setProcessStatus("303002");
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(tsList);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        //m3u8文件url
        mediaFile.setFileUrl(mediaFile.getFilePath() + "hls/" + m3u8Name);
        mediaFileRepository.save(mediaFile);
    }


}
