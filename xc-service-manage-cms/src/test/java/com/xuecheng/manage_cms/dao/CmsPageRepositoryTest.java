package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testFindPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        CmsPage cmsPage = new CmsPage();
        //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setTemplateId("5ad9a24d68db5239b8fef199");
        ExampleMatcher matcher = ExampleMatcher.matching();
        Example<CmsPage> example = Example.of(cmsPage,matcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        System.out.println(all);
    }

    @Test
    public void testFindByPageName() {
        CmsPage cmsPage = cmsPageRepository.findByPageName("index.html").orElse(null);
        if(cmsPage!=null){
            System.out.println(cmsPage);
        }
    }
}
