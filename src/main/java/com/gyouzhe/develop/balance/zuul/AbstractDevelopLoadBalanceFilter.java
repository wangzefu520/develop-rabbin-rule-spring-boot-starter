package com.gyouzhe.develop.balance.zuul;

import com.netflix.zuul.ZuulFilter;

/**
 * 业务
 * <p>
 * author wangchuan
 * since 2021-11-01
 */
public abstract class AbstractDevelopLoadBalanceFilter extends ZuulFilter {

    protected final static String DEVELOP_CLOUD_ATTR_NAME = "develop-env-attr-name";

    protected final static String DEVELOP_CLOUD_ATTR_VALUE = "develop-env-attr-value";

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }
}
