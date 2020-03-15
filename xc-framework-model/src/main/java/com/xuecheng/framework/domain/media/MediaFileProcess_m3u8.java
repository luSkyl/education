package com.xuecheng.framework.domain.media;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@Data
@ToString
public class MediaFileProcess_m3u8 extends MediaFileProcess {

    /**
     * ts列表
     */
    private List<String> tslist;

}
