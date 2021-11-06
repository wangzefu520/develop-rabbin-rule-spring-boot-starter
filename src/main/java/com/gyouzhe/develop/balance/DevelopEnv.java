package com.gyouzhe.develop.balance;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.commons.util.InetUtils;

/**
 * 业务
 * <p>
 * author wangchuan
 * since 2021-11-02
 */
class DevelopEnv {
    String key;
    String primaryValue;
    String backValue;

    private DevelopEnv(String key, String primaryValue, String backValue) {
        this.key = key;
        this.primaryValue = primaryValue;
        this.backValue = backValue;
    }

    public static DevelopEnv getDevelopEnv(DevelopLoadBalanceProperties props, InetUtils inetUtils) {
        if (props == null) {
            props = new DevelopLoadBalanceProperties();
        }
        String key = props.getKey();
        String value = props.getPrimaryValue();
        if (StringUtils.isBlank(value)) {
            value = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        }
        return new DevelopEnv(key, value, props.getBackValue());
    }
}
