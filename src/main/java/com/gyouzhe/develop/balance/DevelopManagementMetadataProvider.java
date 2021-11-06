package com.gyouzhe.develop.balance;

import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.metadata.DefaultManagementMetadataProvider;
import org.springframework.cloud.netflix.eureka.metadata.ManagementMetadata;

import java.util.Map;

/**
 * 处理服务实体注入时的metadata数据
 * <p>
 * author wangchuan
 * since 2021-11-01
 */
public class DevelopManagementMetadataProvider extends DefaultManagementMetadataProvider {

    private String key;
    private String value;

    public DevelopManagementMetadataProvider(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public ManagementMetadata get(EurekaInstanceConfigBean instance, int serverPort, String serverContextPath, String managementContextPath, Integer managementPort) {
        Map<String, String> metadataMap = instance.getMetadataMap();
        metadataMap.put(key, value);
        return super.get(instance, serverPort, serverContextPath, managementContextPath, managementPort);
    }


}
