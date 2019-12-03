# FileManagerServer

## 简介

&emsp;基于 SpringBoot、Jdbc、MySQL、Layui实现的简易文件管理服务器

## 使用技术

| 描述     | 框架                            |
| :------- | :------------------------------ |
| 核心框架 | Spring、Spring Boot、Spring MVC |
| 持久层   | JDBC                            |
| 前端框架 | Layui                           |

## 导入项目

1. 使用 IDEA 选择 Open 导入项目；
2. 导入数据库到MySQL中，sql 位于根目录doc文件夹；
3. 确认applicationyml 配置数据库连接；
4. 分别启动服务端与客户端项目，浏览器访问 `http://localhost:8081/`。 

## 项目结构

```text
|-applog                                                 // 存放日志文件
|-doc                                                    // 存放sql文件
|-common
	|-src                                            
        |-main
           |-java
           |    |-com.oom
           |         |-model                             // 实体类           
           |         |-utils                             // 工具类             
|-client
	|-src                                            
        |-main
           |-java
           |    |-com.oom
           |         |-config                            // 存放客户端SpringBoot配置类
           |         |    |-FileClientConfiger.java      // 文件客户端配置
           |         |-controller                    	 // 控制层 
           |         |-FileClientApplication.java        // SpringBoot客户端启动类              
           |-resources
                |-static                                 // 静态文件
                |    |-assets                            // 资源文件夹
                |    |-index.html                        // 首页
                |
                |-application.yml                        // 配置文件
|-server
	|-src                                            
        |-main
           |-java
           |    |-com.oom
           |         |-config                            // 存放服务端SpringBoot配置类
           |         |	  |-FileServerConfiger.java      // 文件客户端配置
           |         |	  |-FilterConfig.java            // 认证配置
           |         |-controller                    	 // 对外接口层 
           |		|-dao                    	        // 持久层 
           |		|-filter                    	    // 认证层 
           |		|-service                    	    // 业务层 
           |         |-FileServerApplication.java        // SpringBoot服务端启动类              
           |-resources     
                |-application.yml                        // 配置文件
                |-logback-spring.xml                     // 日志配置
                
```

## 项目截图

![首页](https://github.com/HungKuei/FileManagerServer/blob/master/doc/index.png)