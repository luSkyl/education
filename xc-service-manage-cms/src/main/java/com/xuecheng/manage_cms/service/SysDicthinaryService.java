package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms.dao.SysDicthinaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description
 */
@Service
@Slf4j
public class SysDicthinaryService {
    @Autowired
    private SysDicthinaryRepository sysDicthinaryRepository;

    /**
     * 根据字典类型获取数据字典
     * @param type
     * @return
     */
    public SysDictionary get(String type){
        SysDictionary sysDictionary = sysDicthinaryRepository.findByDType(type);
        if(sysDictionary == null){
            log.error("【获取数字字典】 获取数字字典失败 e:{}",CommonCode.INVALID_PARAM.message());
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        return sysDictionary;
    }
}
