package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */

@ToString
public enum LearningCode implements ResultCode {
    /**
     * 获取学习地址失败
     */
    LEARNING_GETMEDIA_ERROR(false,23001,"获取学习地址失败"),
    /**
     * 选课用户为空
     */
    CHOOSECOURSE_USERISNULL(false,23002,"选课用户为空"),
    /**
     * 选课任务为空
     */
    CHOOSECOURSE_TASKISNULL(false,23003,"选课任务为空"),

    ;


    /**
     * 操作是否成功
     */
    boolean success;
    /**
     * 操作代码
     */
    int code;

    /**
     * 提示信息
     */
    String message;

    private LearningCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
