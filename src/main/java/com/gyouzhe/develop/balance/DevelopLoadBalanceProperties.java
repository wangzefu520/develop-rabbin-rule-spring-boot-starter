package com.gyouzhe.develop.balance;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 开发负载参数
 *
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/30 23:46
 **/
@ConfigurationProperties(prefix = DevelopLoadBalanceProperties.PREFIX)
public class DevelopLoadBalanceProperties {

    public static final String PREFIX = "develop.env";
    /**
     * 开发区别key
     */
    private String key = "env-develop";
    /**
     * 区别开发环境的主值
     */
    private String primaryValue;
    /**
     * 第二优先选择的环境
     */
    private String backValue;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrimaryValue() {
        return primaryValue;
    }

    public void setPrimaryValue(String primaryValue) {
        this.primaryValue = primaryValue;
    }

    public String getBackValue() {
        return backValue;
    }

    public void setBackValue(String backValue) {
        this.backValue = backValue;
    }
}
