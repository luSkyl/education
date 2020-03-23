package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 分类模型节点
 */
@Data
@ToString
public class CategoryNode extends Category {

    List<CategoryNode> children;

}
