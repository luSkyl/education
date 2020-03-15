package com.xuecheng.framework.domain.ucenter.ext;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@Data
@ToString
public class XcUserExt extends XcUser {

    /**
     * 权限信息
     */
    private List<XcMenu> permissions;

    /**
     * 企业信息
     */
    private String companyId;
}
