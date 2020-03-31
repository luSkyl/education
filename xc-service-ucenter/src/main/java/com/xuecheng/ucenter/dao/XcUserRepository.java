package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lcy
 * @Date 2020/3/30
 * @Description
 */
public interface XcUserRepository extends JpaRepository<XcUser, String> {
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    XcUser findXcUserByUsername(String username);
}
