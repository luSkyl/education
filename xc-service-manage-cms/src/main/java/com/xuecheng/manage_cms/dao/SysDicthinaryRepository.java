package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description
 */
public interface SysDicthinaryRepository extends MongoRepository<SysDictionary,String> {
    /**
     * 根据字典分类进行查找
     * @param dType
     * @return
     */
    SysDictionary findByDType(String dType);

}
