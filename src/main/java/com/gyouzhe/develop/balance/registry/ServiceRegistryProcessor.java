package com.gyouzhe.develop.balance.registry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理服务注册
 *
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/31 10:15
 **/
public class ServiceRegistryProcessor implements BeanPostProcessor, ApplicationContextAware {

    private static final String REGISTER_METHOD_NAME = "register";

    private final String key;

    private final String val;

    private ApplicationContext applicationContext;

    public ServiceRegistryProcessor(String key, String val) {
        this.key = key;
        this.val = val;
    }

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ServiceRegistry) {
            return getProxyBean(bean);
        }
        return bean;
    }

    private Object getProxyBean(Object bean) {
        Class<?> clazz = bean.getClass();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> {
            String methodName = method.getName();
            if (StringUtils.equals(methodName, REGISTER_METHOD_NAME)) {
                Registration registration = (Registration) args[0];
                registration.getMetadata().put(key, val);
            }
            return methodProxy.invokeSuper(o, args);
        });
        Constructor<?>[] crs = clazz.getConstructors();
        for (Constructor<?> constructor : crs) {
            Class<?>[] types = constructor.getParameterTypes();
            if (types.length == 0) {
                return enhancer.create();
            }
            List<Object> args = new ArrayList<>(types.length);
            for (Class<?> cls : types) {
                Object obj = applicationContext.getBean(cls);
                args.add(obj);
            }
            return enhancer.create(types, args.toArray(new Object[0]));
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
