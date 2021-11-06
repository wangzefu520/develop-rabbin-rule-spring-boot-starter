## 开发环境负载工具
### 介绍
本工具主要目是在分布式环境开发中，存在开发测试环境时，本地开发想使用开发环境，又想本地请求接口能完整的负载到本地调试，或者想使用测试环境但是又想和同事联调接口则可使用本工具。本工具旨在让开发的小伙伴开发分布式系统更加快捷方便

### 兼容环境
本工具需要系统使用ribbon，兼容注册中心为nacos和eureka

### 使用说明
1、将项目导入到本地执行如下命名，确保包能导入到本地
```
mvn clean compile package install -DskipTests
```  
2、在项目POM依赖文件中添加如下依赖
```xml
<dependency>
    <groupId>com.gyouzhe</groupId>
    <artifactId>develop-rabbin-rule-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```  
3、在项目配置文件中加上启动配置（插件默认为不启用状态)
```yaml
develop:
  env:
    enable: true
```  
4、启动应用查看注册到注册中心（eureka,nacos）中服务metadata中是否已经有了cloud-develop属性


### 开发metadata属性修改
```yaml
develop:
  cloud:
    key: 特定负载区分key
    primary-value: 主特定负载区分值
    back-value: 备特定负载区分值（当且仅当没有主值才会采用备值作为负载值）
```