## DubboDemo 简单上手

### 简介
这个是我学习SpringBoot整合Dubbo和Zookeeper快速上手案例，项目有点粗糙，还在不断改进  
SpringBoot的两个服务和Dubbo都在本机（Win）运行，Zookeeper 部署在Linux服务器的Docker容器中  
* 父工程：dubbo_test
  * api：接口定义
  * controller：相当于消费者角色
  * user：相当于生产者角色

大致流程就是在`api`模块中定义好接口，打成`jar`包交给父工程用作依赖，子模块`controller` `user`跟着父模块也继承了`api`模块，然后一个用来实现服务类，另一个用来调用服务类

### 要求
Maven环境，Linux中要有Docker环境

### 开始

#### 搭建聚合性工程

父类工程使用 SpringBoot 创建，只保留`pom.xml`文件，其三个子模块也分别使用 SpringBoot 创建

设置父工程打包方式

```xml
<groupId>com.example</groupId>
<artifactId>dubbo_test</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name>dubbo_test</name>
<description>Demo project for Spring Boot</description>
<packaging>pom</packaging>
```

在父工程pom.xml 文件中声明子模块

```xml
    <modules>
        <module>user</module>
        <module>controller</module>
        <module>api</module>
    </modules>
```

**子模块`api`**中设置打包方式为`jar`，这是因为需要用作父工程的依赖

```xml
<groupId>com.example</groupId>
    <artifactId>api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>api</name>
    <description>api module</description>
    <packaging>jar</packaging>
```

父工程添加依赖（包括子模块`api`），这里暂时没用上`zookeeper`，所以不添加

```xml
  <dependencies>
       <!-- springboot 常规依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <!-- Dubbo -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>3.0.3</version>
         <!-- 排除 log4j 依赖 -->
            <exclusions>
                <exclusion>
                    <groupId>log4j</groupId>
                    <artifactId>log4j</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- api模块 -->
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

然后在子模块中指明父工程（这个操作只针对user模块和controller模块，api模块不需要！）

```xml
<!-- 如果在api模块中引入下面这个会造成循环依赖-->
<parent>
        <artifactId>dubbo_test</artifactId>
        <groupId>com.example</groupId>
        <version>0.0.1-SNAPSHOT</version>
</parent>
```

controller 模块加入web依赖

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

至此，基本的模块构建完毕！



#### Dubbo配置

由于引入了dubbo-springboot-starter，所以只需要在配置文件中简单配置

user模块的`application.properties`

```properties
dubbo.application.name=provider
# dubbo 支持多种协议，这里使用dubbo协议
dubbo.protocol.name=dubbo
# 该服务暴露的端口
dubbo.protocol.port=20880
# 扫描包，类似mybatis
dubbo.scan.base-packages=com.example.user.service
# 注册地址选无
dubbo.registry.address=N/A
```

controller 模块

```properties
dubbo.application.name=consumer
dubbo.scan.base-packages=com.example.controller.Controller
```



#### 简单开发

* api 模块定义接口

```java
public interface UserService {
    public String login(String username);
}
```

* user 模块实现接口,加上`@DubboService`注解标识该服务可以被远程调用

```java
@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {
    @Override
    public String login(String username) {
        return username;
    }
}
```

* controller 模块

```java
@RestController
public class testController {
    Logger logger = LoggerFactory.getLogger(testController.class);
    
    //远程服务注入
    @DubboReference(version = "1.0.0",
    url = "dubbo://localhost:20880")
    private UserService userService;

    //一个Mapping，调用服务实现功能
    @GetMapping("/dubboTest/{msg}")
    public String method1(@PathVariable("msg") String msg) {
       return userService.login("controller give you a " + msg);
    }

}
```
经过测试，可以完美运行，现在开始整合 Zookeeper  
由于不想在 Linux中 配置 Zookeeper，所以我们直接使用 Zookeeper 的 Docker 镜像
[Zookeeper官方镜像](https://hub.docker.com/_/zookeeper)  
执行命令：
`docker pull zookeeper`  
下载完成，查看一下  
`docker images`  
运行
`docker run --name zookeeper -p 2181:2181 --restart always -d zookeeper`









