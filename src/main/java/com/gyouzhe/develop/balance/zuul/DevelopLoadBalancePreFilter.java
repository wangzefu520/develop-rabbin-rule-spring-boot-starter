package com.gyouzhe.develop.balance.zuul;

import com.gyouzhe.develop.balance.DevelopLoadBalanceContextHolder;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取传入过来的开发负载均衡参数过滤器，如果没有获取到则填写自己的IP
 * <p>
 * author wangchuan
 * since 2021-11-01
 */
public class DevelopLoadBalancePreFilter extends AbstractDevelopLoadBalanceFilter {

    private final String key;
    private final String value;

    public DevelopLoadBalancePreFilter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String tempHeadValue = request.getHeader(key);
        String developCloudValue = value;
        if (StringUtils.isNotBlank(tempHeadValue)) {
            developCloudValue = tempHeadValue;
        }
        DevelopLoadBalanceContextHolder.setVal(developCloudValue);
        context.set(DEVELOP_CLOUD_ATTR_NAME, key);
        context.set(DEVELOP_CLOUD_ATTR_VALUE, developCloudValue);
        return null;
    }
}
