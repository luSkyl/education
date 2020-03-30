package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 自定义课程计划结点类
 */
@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

    //媒资信息
    //媒资文件Id
    private String mediaId;
    //媒资文件原始名称
    private String mediaFileOriginalName;

}
