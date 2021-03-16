# jyrdexam
公司员工在线考试系统

当前为系统后端接口以及管理员页面

# 软件架构：SpringBoot+Mybatis-Plus+jQuery+Bootstrap+Ajax

演示地址：http://admin.lvyi.club    员工答题前端演示地址：http://exam.lvyi.club

## 技术栈

1. 主技术栈

* [redis](https://github.com/redis/redis)
* [FastDFS](https://github.com/happyfish100/fastdfs)
* [Quartz](https://github.com/quartz-scheduler/quartz)
* [AutoGenerator自动代码构建](https://mp.baomidou.com/config/generator-config.html)
* 多线程
* 阿里云SMS

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
```

## License

[MIT](https://opensource.org/licenses/MIT)
