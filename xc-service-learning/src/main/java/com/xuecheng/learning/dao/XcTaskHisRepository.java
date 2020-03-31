package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.task.XcTaskHis;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lcy
 * @Date 2020/3/31
 * @Description 历史任务Dao
 */
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis,String> {
}
