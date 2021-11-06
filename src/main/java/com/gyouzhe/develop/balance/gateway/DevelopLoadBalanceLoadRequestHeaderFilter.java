package com.gyouzhe.develop.balance.gateway;

import com.gyouzhe.develop.balance.DevelopLoadBalanceContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;

/**
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/31 23:32
 **/
public class DevelopLoadBalanceLoadRequestHeaderFilter extends AbstractDevelopLoadBalanceHeaderFilter {

    private final String key;

    private final String value;

    public DevelopLoadBalanceLoadRequestHeaderFilter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public HttpHeaders filter(HttpHeaders input, ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String developValue = value;
        if (!headers.containsKey(key)) {
            input.put(key, Collections.singletonList(value));
        } else {
            developValue = headers.getFirst(key);
        }
        exchange.getAttributes().put(DEVELOP_ENV_ATTR_NAME, key);
        exchange.getAttributes().put(DEVELOP_ENV_ATTR_VALUE, developValue);
        DevelopLoadBalanceContextHolder.setVal(developValue);
        return input;
    }

    @Override
    public boolean supports(Type type) {
        return type.equals(Type.REQUEST);
    }
}
