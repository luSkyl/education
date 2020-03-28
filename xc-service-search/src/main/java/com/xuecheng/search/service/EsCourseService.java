package com.xuecheng.search.service;

import cn.hutool.core.util.PageUtil;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.response.QueryResponseResult;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author lcy
 * @Date 2020/3/27
 * @Description
 */
@Service
@Slf4j
public class EsCourseService {

    @Value("${xuecheng.elasticsearch.course.index}")
    private String esIndex;
    @Value("${xuecheng.elasticsearch.course.type}")
    private String esType;
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String sourceField;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 查询课程
     *
     * @param page
     * @param size
     * @param courseSearchParam
     * @return
     */
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        //设置索引
        SearchRequest searchRequest = new SearchRequest(esIndex);
        //设置类型
        searchRequest.types(esType);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过滤源字段
        String[] sourceFields = sourceField.split(",");
        searchSourceBuilder.fetchSource(sourceFields, new String[]{});
        //创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (StringUtils.isNotBlank(courseSearchParam.getKeyword())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "teachplan", "description")
                    .minimumShouldMatch("70%")
                    .field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        //过虑
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }
        //分页
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 20;
        }
        searchSourceBuilder.from(PageUtil.getStart(page, size));
        searchSourceBuilder.size(size);
        //布尔查询
        searchSourceBuilder.query(boolQueryBuilder);
        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //请求搜索
        searchRequest.source(searchSourceBuilder);

        QueryResult<CoursePub> queryResult = new QueryResult<>();
        //执行搜索
        SearchResponse search = null;
        try {
            //执行搜索
            search = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            log.error("【ES根据关键字进行搜索】 ES根据关键字进行搜索失败，e:{}", e);
            e.printStackTrace();
            return new QueryResponseResult(CommonCode.SUCCESS, new QueryResult<CoursePub>());
        }
        //获取相应结果
        SearchHits hits = search.getHits();
        //匹配的总记录数
        queryResult.setTotal(hits.getTotalHits());
        SearchHit[] searchHits = hits.getHits();
        //数据列表
        List<CoursePub> coursePubList = new ArrayList<>();

        for (SearchHit searchHit : searchHits) {
            CoursePub coursePub = new CoursePub();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String id = (String) sourceAsMap.get("id");
            coursePub.setId(id);
            //名字
            String name = (String) sourceAsMap.get("name");
            //取出高亮字段内容
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            if(highlightFields!=null){
                HighlightField nameField = highlightFields.get("name");
                if(nameField!=null){
                    Text[] fragments = nameField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text str : fragments) {
                        stringBuffer.append(str.string());
                    }
                    name = stringBuffer.toString();
                }
            }
            coursePub.setName(name);
            //图片
            String pic = (String) sourceAsMap.get("pic");
            coursePub.setPic(pic);
            //价格
            Double price = null;
            try {
                if (sourceAsMap.get("price") != null) {
                    price = (Double) sourceAsMap.get("price");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            coursePub.setPrice(price);
            Float priceOld = null;
            try {
                if (sourceAsMap.get("price_old") != null) {
                    priceOld = Float.parseFloat((String) sourceAsMap.get("price_old"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            coursePub.setPrice_old(priceOld);
            coursePubList.add(coursePub);
        }
        queryResult.setList(coursePubList);

        QueryResponseResult<CoursePub> coursePubQueryResponseResult = new QueryResponseResult<CoursePub>(CommonCode.SUCCESS, queryResult);
        return coursePubQueryResponseResult;
    }

}
