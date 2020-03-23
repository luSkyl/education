package com.xuecheng.manage_course.sercvice;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import com.xuecheng.manage_course.dao.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author lcy
 * @Date 2020/3/19
 * @Description
 */
@Service
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryNode findList() {
        CategoryNode categoryNode = categoryMapper.selectCategoryList();
        Category category = categoryRepository.findById("1").get();
        categoryNode.setIsleaf(category.getIsleaf());
        categoryNode.setIsshow(category.getIsshow());
        return categoryNode;
    }

    public Category findOne(String id){
        Optional<Category> category = categoryRepository.findById(id);
        if(!category.isPresent()){
            log.error("【查询课程分类】 查询课程分类失败 e:{}",CommonCode.INVALID_PARAM.message());
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        return category.get();
    }

}
