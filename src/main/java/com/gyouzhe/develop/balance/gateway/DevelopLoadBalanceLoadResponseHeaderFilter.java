package com.gyouzhe.develop.balance.gateway;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/31 23:43
 **/
public class DevelopLoadBalanceLoadResponseHeaderFilter extends AbstractDevelopLoadBalanceHeaderFilter {
    @Override
    public HttpHeaders filter(HttpHeaders input, ServerWebExchange exchange) {
        String key = exchange.getAttribute(DEVELOP_ENV_ATTR_NAME);
        String val = exchange.getAttribute(DEVELOP_ENV_ATTR_VALUE);
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(val)) {
            input.set(key, val);
        }
        return input;
    }

    @Override
    public boolean supports(Type type) {
        return type.equals(Type.RESPONSE);
    }
}
