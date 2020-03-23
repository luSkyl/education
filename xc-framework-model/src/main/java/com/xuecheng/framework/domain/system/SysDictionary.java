package com.xuecheng.framework.domain.system;

import com.xuecheng.framework.domain.cms.CmsConfigModel;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description 数据字典模型
 */
@Data
@ToString
@Document(collection = "sys_dictionary")
public class SysDictionary {

    @Id
    private String id;

    /**
     * 字典名称
     */
    @Field("d_name")
    private String dName;

    /**
     * 字典分类
     */
    @Field("d_type")
    private String dType;

    /**
     * 字典数据
     */
    @Field("d_value")
    private List<SysDictionaryValue> dValue;

}
