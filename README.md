# jyrdexam
公司员工在线考试系统

当前为系统后端接口以及管理员页面

软件架构：SpringBoot+Mybatis-Plus+jQuery+Bootstrap+Ajax

演示地址：http://admin.lvyi.club  &nbsp;&nbsp;员工答题[前端](https://github.com/lovbe0210/jyrdexamFont)演示地址：http://exam.lvyi.club

## 技术栈

1. 主技术栈

* [redis](https://github.com/redis/redis)
* [FastDFS](https://github.com/happyfish100/fastdfs)
* [Quartz](https://github.com/quartz-scheduler/quartz)
* [AutoGenerator自动代码构建](https://mp.baomidou.com/config/generator-config.html)
* 多线程
* 阿里云SMS
* log4j日志

2. UI 技术栈
* 模板引擎Thyemleaf
* Bootstrap
* Ajax

## 项目结构

```java
|-com.jyrd.exam
    |-base
      |-common //公共模块
      |-config //配置文件
      |-entity //数据库实体类
      |-mapper //mapper接口以及xml
      |-vo //参数传递实体
    |-admin  //管理员api
    |-home   //入口api
    |-exam   //考试相关api 
|-resources
    |-static  //页面静态文件
    |-templates  //模板视图文件
    |-SQL  //SQL语句
    application.yml //核心配置文件
    logback-spring.xml //日志配置文件
    tracker.conf  //fastDFS配置文件
```

## 项目部署
1. FastDFS安装
* 先安装tracker服务
* 然后安装storage服务
* 最后安装fastdfs-nginx-module

2. nginx安装
* nginx.conf配置
```java
\#前端项目根路径
    location / {
        root   jyrdexam; // 该路径为nginx安装目录下的项目路径
        try_files $uri $uri/ @router;
        index  index.html index.htm;
    }
\#解决vue刷新页面404
    location @router {
        rewrite ^.*$ /index.html last;
    }
\#fastDFS
    location /group1/M00/ {
         ngx_fastdfs_module;
    }
\#后端接口转发
     location /ucenter/ {
        proxy_pass   http://exam-admin;
    }
    location /exam/ {
        proxy_pass   http://exam-admin;
    }
```


## License

[MIT](https://opensource.org/licenses/MIT)
