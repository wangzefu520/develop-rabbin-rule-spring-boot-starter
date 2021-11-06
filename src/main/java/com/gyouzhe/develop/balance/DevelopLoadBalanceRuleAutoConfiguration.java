package com.gyouzhe.develop.balance;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 开发负载规则自动注入
 * <p>
 * author wangchuan
 * since 2021-11-02
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnBean(SpringClientFactory.class)
@AutoConfigureAfter(RibbonAutoConfiguration.class)
@RibbonClients(defaultConfiguration = DevelopLoadBalanceRuleClientConfiguration.class)
public class DevelopLoadBalanceRuleAutoConfiguration {
}
