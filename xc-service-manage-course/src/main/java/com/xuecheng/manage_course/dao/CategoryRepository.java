package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description
 */
public interface CategoryRepository extends JpaRepository<Category,String> {
}
