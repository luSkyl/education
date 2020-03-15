package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Teachplan;
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
public class TeachplanParameter extends Teachplan {

    /**
     * 二级分类ids
     */
    List<String> bIds;
    /**
     * 级分类ids
     */
    List<String> cIds;

}
