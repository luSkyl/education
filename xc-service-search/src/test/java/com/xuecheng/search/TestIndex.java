package com.xuecheng.search;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @Author lcy
 * @Date 2020/3/23
 * @Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private RestClient restClient;

    /**
     * 创建索引库
     * @throws IOException
     */
    @Test
    public void testCreateIndex() throws IOException {

    }


    /**
     * 删除索引库
     * @throws IOException
     */
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest xc_course = new DeleteIndexRequest("xc_course");
        IndicesClient indices = client.indices();
        DeleteIndexResponse delete = indices.delete(xc_course);
        System.out.println(delete.isAcknowledged());
    }


    /**
     * 查询所有
     * @throws IOException
     */
    @Test
    public void testSearchAll() throws IOException {
        //请求搜索对象
        SearchRequest xc_course = new SearchRequest("xc_course");
        //设置类型
        xc_course.types("doc");
        //构建源对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //sourse源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","description"},new String[]{});
        //设置搜索源
        xc_course.source(searchSourceBuilder);
        //执行搜索
        SearchResponse search = client.search(xc_course);
        //搜索匹配结果
        SearchHits hits = search.getHits();
        long totalHits = hits.getTotalHits();
        SearchHit[] hitsHits = hits.getHits();
        for (SearchHit hitsHit : hitsHits) {
            Map<String, Object> sourceAsMap = hitsHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            Double price = (Double) sourceAsMap.get("price");
            System.out.println("name="+name);
            System.out.println("studymodel="+studymodel);
            System.out.println("description="+description);
            System.out.println("price="+price);
        }

    }
}
