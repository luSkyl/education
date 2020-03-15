package com.xuecheng.framework.domain.report;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@Data
@ToString
@Document(collection = "report_course")
public class ReportCourse {

    @Id
    private String id;
    /**
     * 评价分数
     */
    private Float evaluation_score;
    /**
     * 收藏次数
     */
    private Long collect_num;
    /**
     * 播放次数
     */
    private Long play_num;
    /**
     * 学生人数
     */
    private Long student_num;
    /**
     * 课程时长
     */
    private Long timelength;

}
