package com.gyouzhe.develop.balance.ribbon;

import com.gyouzhe.develop.balance.DevelopLoadBalanceContextHolder;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 开发者规则
 *
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/30 23:02
 **/
public class DevelopRule extends AbstractLoadBalancerRule {

    private final RandomRule randomRule;

    private String key;

    private String primaryValue;

    private String backValue;

    public DevelopRule() {
        randomRule = new RandomRule();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPrimaryValue(String primaryValue) {
        this.primaryValue = primaryValue;
    }

    public void setBackValue(String backValue) {
        this.backValue = backValue;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        List<Server> servers = lb.getReachableServers();
        if (!CollectionUtils.isEmpty(servers)) {
            String tempFilterValue = DevelopLoadBalanceContextHolder.getVal();
            String filterValue = tempFilterValue == null ? this.primaryValue : tempFilterValue;
            String backValue = this.backValue;
            Server backServer = null;
            for (Server server : servers) {
                Optional<Object> opt = getMetadata(server);
                if (opt.isPresent()) {
                    Object obj = opt.get();
                    if (obj instanceof Map) {
                        Map<String, String> metadata = (Map<String, String>) obj;
                        if (metadata.containsKey(this.key)) {
                            String metadataValue = metadata.get(this.key);
                            if (StringUtils.contains(filterValue, ",")) {
                                String[] filters = StringUtils.split(filterValue, ",");
                                if (ArrayUtils.contains(filters, metadataValue)) {
                                    return server;
                                }
                            } else {
                                if (StringUtils.equals(filterValue, metadataValue)) {
                                    return server;
                                }
                            }
                            if (StringUtils.isNotBlank(backValue)) {
                                if (StringUtils.contains(backValue, ",")) {
                                    String[] filters = StringUtils.split(backValue, ",");
                                    if (ArrayUtils.contains(filters, metadataValue)) {
                                        return server;
                                    }
                                } else {
                                    if (StringUtils.equals(backValue, metadataValue)) {
                                        backServer = server;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (null != backServer) {
                return backServer;
            }
        }
        return randomRule.choose(getLoadBalancer(), key);
    }

    /**
     * 获取实体的某个字段值
     *
     * @param obj 对象
     * @return 字段值
     */
    private Optional<Object> getMetadata(final Object obj) {
        try {
            Object holdObj = obj;
            Class<?> clazz = holdObj.getClass();
            Field field = ReflectionUtils.findField(clazz, "metadata");
            if (null == field) {
                field = ReflectionUtils.findField(clazz, "instanceInfo");
                if (null != field) {
                    field.setAccessible(true);
                    Object instanceInfo = field.get(holdObj);
                    if (null != instanceInfo) {
                        field = ReflectionUtils.findField(instanceInfo.getClass(), "metadata");
                        holdObj = instanceInfo;
                    }
                }
            }
            if (null != field) {
                field.setAccessible(true);
                Object objVal = field.get(holdObj);
                if (null != objVal) {
                    return Optional.of(objVal);
                }
            }
        } catch (IllegalAccessException ignore) {
        }
        return Optional.empty();
    }
}
