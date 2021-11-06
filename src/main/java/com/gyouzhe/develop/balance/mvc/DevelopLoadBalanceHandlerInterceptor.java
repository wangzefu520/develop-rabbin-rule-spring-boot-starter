package com.gyouzhe.develop.balance.mvc;

import com.gyouzhe.develop.balance.DevelopLoadBalanceContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 开发interceptor
 *
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/31 22:42
 **/
public class DevelopLoadBalanceHandlerInterceptor extends HandlerInterceptorAdapter {

    private final String key;
    private final String value;

    public DevelopLoadBalanceHandlerInterceptor(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String developCloudValue = request.getHeader(key);
        if (StringUtils.isBlank(developCloudValue)) {
            developCloudValue = value;
        }
        DevelopLoadBalanceContextHolder.setVal(developCloudValue);
        request.setAttribute(key, developCloudValue);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        String tempValue = (String) request.getAttribute(key);
        response.setHeader(key, tempValue);
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        DevelopLoadBalanceContextHolder.clear();
        super.afterCompletion(request, response, handler, ex);
    }
}
