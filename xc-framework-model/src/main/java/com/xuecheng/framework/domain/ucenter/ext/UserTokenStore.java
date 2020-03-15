package com.xuecheng.framework.domain.ucenter.ext;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author lcy
 * @Date 2020/3/12
 * @Description
 */
@Data
@ToString
@NoArgsConstructor
public class UserTokenStore extends AuthToken {
    /**
     * 用户id
     */
    String userId;
    /**
     * 用户类型
     */
    String utype;
    /**
     * 用户所属企业信息
     */
    String companyId;

}
