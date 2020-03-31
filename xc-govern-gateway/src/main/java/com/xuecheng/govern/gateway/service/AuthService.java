package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author lcy
 * @Date 2020/3/31
 * @Description
 */
@Service
@Slf4j
public class AuthService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询身份令牌
     *
     * @param request
     * @return
     */
    public String getTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String access_token = cookieMap.get("uid");
        if (StringUtils.isEmpty(access_token)) {
            return null;
        }
        return access_token;
    }

    /**
     * 从header中查询jwt令牌
     *
     * @param request
     * @return
     */
    public String getJwtFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            //拒绝访问
            return null;
        }
        if (!authorization.startsWith("Bearer ")) {
            //拒绝访问
            return null;
        }
        return authorization;
    }

    /**
     * 查询令牌的有效期
     *
     * @param access_token
     * @return
     */
    public long getExpire(String access_token) {
        //token在redis中的key
        String key = "user_token:" + access_token;
        Long expire = stringRedisTemplate.getExpire(key);
        return expire;
    }


}
