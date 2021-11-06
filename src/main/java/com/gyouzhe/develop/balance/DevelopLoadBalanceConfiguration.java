package com.gyouzhe.develop.balance;

import com.gyouzhe.develop.balance.gateway.DevelopLoadBalanceLoadRequestHeaderFilter;
import com.gyouzhe.develop.balance.gateway.DevelopLoadBalanceLoadResponseHeaderFilter;
import com.gyouzhe.develop.balance.mvc.DevelopLoadBalanceHandlerInterceptor;
import com.gyouzhe.develop.balance.registry.ServiceRegistryProcessor;
import com.gyouzhe.develop.balance.zuul.DevelopLoadBalancePostFilter;
import com.gyouzhe.develop.balance.zuul.DevelopLoadBalancePreFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 开发负载均衡处理
 *
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/30 23:01
 **/
@Configuration
@ConditionalOnProperty(value = "develop.env.enable", havingValue = "true")
@EnableConfigurationProperties(DevelopLoadBalanceProperties.class)
public class DevelopLoadBalanceConfiguration {

    @Autowired(required = false)
    private DevelopLoadBalanceProperties props;

    /**
     * 系统信息获取工具
     *
     * @param inetUtilsProperties 系统信息获取默认参数
     * @return 系统信息获取工具
     */
    @Bean
    @ConditionalOnMissingBean(InetUtils.class)
    public InetUtils inetUtils(@Autowired(required = false) InetUtilsProperties inetUtilsProperties) {
        if (null == inetUtilsProperties) {
            inetUtilsProperties = new InetUtilsProperties();
        }
        return new InetUtils(inetUtilsProperties);
    }


    @Bean
    @ConditionalOnClass(ServiceRegistry.class)
    public ServiceRegistryProcessor serviceRegistryProcessor(InetUtils inetUtils) {
        DevelopEnv developEnv = getDevelopEnv(props, inetUtils);
        return new ServiceRegistryProcessor(developEnv.key, developEnv.primaryValue);
    }


    @Configuration
    @ConditionalOnClass(name = "org.springframework.cloud.netflix.eureka.metadata.ManagementMetadataProvider")
    protected static class EurekaConfiguration {

        @Autowired(required = false)
        private DevelopLoadBalanceProperties props;

        @Autowired(required = false)
        private InetUtils inetUtils;

        @Bean
        public DevelopManagementMetadataProvider serviceManagementMetadataProvider() {
            DevelopEnv developEnv = getDevelopEnv(props, inetUtils);
            return new DevelopManagementMetadataProvider(developEnv.key, developEnv.primaryValue);
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass(name = "org.springframework.cloud.gateway.filter.GatewayFilterChain")
    protected static class GatewayConfiguration {

        @Autowired(required = false)
        private DevelopLoadBalanceProperties props;

        @Autowired(required = false)
        private InetUtils inetUtils;

        @Bean
        public DevelopLoadBalanceLoadRequestHeaderFilter developLoadBalanceLoadRequestHeaderFilter() {
            DevelopEnv developEnv = getDevelopEnv(props, inetUtils);
            return new DevelopLoadBalanceLoadRequestHeaderFilter(developEnv.key, developEnv.primaryValue);
        }

        @Bean
        public DevelopLoadBalanceLoadResponseHeaderFilter developLoadBalanceLoadResponseFilter() {
            return new DevelopLoadBalanceLoadResponseHeaderFilter();
        }
    }


    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass(name = "org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping")
    protected static class ZuulConfiguration {

        @Autowired(required = false)
        private DevelopLoadBalanceProperties props;

        @Autowired(required = false)
        private InetUtils inetUtils;

        @Bean
        public DevelopLoadBalancePreFilter developLoadBalancePreFilter() {
            DevelopEnv developEnv = getDevelopEnv(props, inetUtils);
            return new DevelopLoadBalancePreFilter(developEnv.key, developEnv.primaryValue);
        }

        @Bean
        public DevelopLoadBalancePostFilter developLoadBalancePostFilter() {
            return new DevelopLoadBalancePostFilter();
        }
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    protected static class WebConfiguration {

        @Autowired(required = false)
        private DevelopLoadBalanceProperties props;

        @Autowired(required = false)
        private InetUtils inetUtils;

        @Bean
        public WebMvcConfigurer developLoadBalanceInterceptor() {
            DevelopEnv developEnv = getDevelopEnv(props, inetUtils);
            DevelopLoadBalanceHandlerInterceptor developLoadBalanceHandlerInterceptor = new DevelopLoadBalanceHandlerInterceptor(developEnv.key, developEnv.primaryValue);
            return new WebMvcConfigurer() {
                @Override
                public void addInterceptors(InterceptorRegistry registry) {
                    registry.addInterceptor(developLoadBalanceHandlerInterceptor);
                }
            };
        }
    }

    private static DevelopEnv getDevelopEnv(DevelopLoadBalanceProperties props, InetUtils inetUtils) {
        return DevelopEnv.getDevelopEnv(props, inetUtils);
    }


}
