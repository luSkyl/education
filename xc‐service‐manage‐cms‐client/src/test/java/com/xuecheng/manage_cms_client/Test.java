package com.xuecheng.manage_cms_client;

import org.bson.types.ObjectId;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @Author lcy
 * @Date 2020/3/22
 * @Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @org.junit.Test
    public void testStore2() throws FileNotFoundException {
        File file = new File("E:\\BaiduNetdiskDownload\\黑马\\21-微服务教育网学成在线\\09-课程预览 Eureka Feign\\资料\\课程详情页面模板\\course.ftl");
        FileInputStream inputStream = new FileInputStream(file);
        //保存模版文件内容
        ObjectId objectId = gridFsTemplate.store(inputStream, "课程详情模板文件","");
        System.out.println( objectId.toString());
        //5e76ca0eb7f79b3e5c15550e  5aec5d8c0e6618376c08e47e
    }
}
