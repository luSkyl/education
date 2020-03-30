package com.xuecheng.framework.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
public class Mp4VideoUtil extends VideoUtil {

    public String ffmpegPath;
    private String videoPath;
    private String mp4Name;
    private String mp4folderPath;

    public Mp4VideoUtil(String ffmpegPath, String videoPath, String mp4Name, String mp4folderPath) {
        super(ffmpegPath);
        this.ffmpegPath = ffmpegPath;
        this.videoPath = videoPath;
        this.mp4Name = mp4Name;
        this.mp4folderPath = mp4folderPath;
    }

    /**
     * 清除已生成的mp4
     *
     * @param mp4Path
     */
    private void clearMp4(String mp4Path) {
        //删除原来已经生成的m3u8及ts文件
        File mp4File = new File(mp4Path);
        if (mp4File.exists() && mp4File.isFile()) {
            mp4File.delete();
        }
    }

    /**
     * 视频编码，生成mp4文件
     *
     * @return 成功返回success，失败返回控制台日志
     */
    public String generateMp4() {
        //清除已生成的mp4
        clearMp4(mp4folderPath + mp4Name);
        /*
        ffmpeg.exe -i  lucene.avi -c:v libx264 -s 1280x720 -pix_fmt yuv420p -b:a 63k -b:v 753k -r 18 .\lucene.mp4
         */
        List<String> commend = new ArrayList<String>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(videoPath);
        commend.add("-c:v");
        commend.add("libx264");
        commend.add("-y");
        commend.add("-s");
        commend.add("1280x720");
        commend.add("-pix_fmt");
        commend.add("yuv420p");
        commend.add("-b:a");
        commend.add("63k");
        commend.add("-b:v");
        commend.add("753k");
        commend.add("-r");
        commend.add("18");
        commend.add(mp4folderPath + mp4Name);
        String outstring = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            //将标准输入流和错误输入流合并，通过标准输入流程读取信息
            builder.redirectErrorStream(true);
            Process p = builder.start();
            outstring = waitFor(p);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Boolean check_video_time = this.check_video_time(videoPath, mp4folderPath + mp4Name);
        if (!check_video_time) {
            return outstring;
        } else {
            return "success";
        }
    }

    public static void main(String[] args) throws IOException {
        String ffmpeg_path = "D:\\Program Files\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe";//ffmpeg的安装位置
        String video_path = "E:\\ffmpeg_test\\1.avi";
        String mp4_name = "809694a6a974c35e3a36f36850837d7c.mp4";
        String mp4_path = "F:/develop/upload/8/0/809694a6a974c35e3a36f36850837d7c/";
        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4_path);
        String s = videoUtil.generateMp4();
        System.out.println(s);
    }
}
