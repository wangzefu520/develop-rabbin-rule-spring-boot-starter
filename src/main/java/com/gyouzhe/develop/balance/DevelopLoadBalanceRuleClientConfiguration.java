package com.gyouzhe.develop.balance;

import com.gyouzhe.develop.balance.ribbon.DevelopRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动注册类
 * author wangchuan
 * since 2021-11-02
 */

@Configuration
public class DevelopLoadBalanceRuleClientConfiguration {

    @Autowired(required = false)
    private DevelopLoadBalanceProperties props;

    @Autowired
    private InetUtils inetUtils;

    @Autowired
    private PropertiesFactory propertiesFactory;

    @RibbonClientName
    private String serviceId = "client";

    public DevelopLoadBalanceRuleClientConfiguration() {
    }

    public DevelopLoadBalanceRuleClientConfiguration(String serviceId) {
        this.serviceId = serviceId;
    }

    @Bean
    public IRule developRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, serviceId)) {
            return this.propertiesFactory.get(IRule.class, config, serviceId);
        }
        DevelopEnv developEnv = DevelopEnv.getDevelopEnv(props, inetUtils);
        DevelopRule rule = new DevelopRule();
        rule.setKey(developEnv.key);
        rule.setPrimaryValue(developEnv.primaryValue);
        rule.setBackValue(developEnv.backValue);
        rule.initWithNiwsConfig(config);
        return rule;
    }
}
