package com.gyouzhe.develop.balance.zuul;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletResponse;

/**
 * 放置开发负载均衡头信息过滤器
 * <p>
 * author wangchuan
 * since 2021-11-01
 */
public class DevelopLoadBalancePostFilter extends AbstractDevelopLoadBalanceFilter {
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse response = context.getResponse();
        String key = (String) context.get(DEVELOP_CLOUD_ATTR_NAME);
        String value = (String) context.get(DEVELOP_CLOUD_ATTR_VALUE);
        response.setHeader(key, value);
        return null;
    }
}
