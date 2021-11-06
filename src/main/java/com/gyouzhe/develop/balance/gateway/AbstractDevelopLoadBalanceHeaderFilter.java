package com.gyouzhe.develop.balance.gateway;

import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;

/**
 * 抽象透信息过滤器
 *
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/31 23:45
 **/
public abstract class AbstractDevelopLoadBalanceHeaderFilter implements HttpHeadersFilter {

    protected static final String DEVELOP_ENV_ATTR_NAME = "develop-env-attr-name";

    protected static final String DEVELOP_ENV_ATTR_VALUE = "develop-env-attr-value";
}
