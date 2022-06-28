# Spring Cloud 学习演练

## VS Code 配置 Spring Cloud 使用 maven 做包管理
参考
https://blog.csdn.net/led1114/article/details/122650051

Ctrl+Shift+P，然后输入 Spring Initializr: Create a Maven project。前几步都选择提示的默认选项即可，到 Specify package type 时，选择 War。Java version 选当前最新的 18。Choose dependencies 选 Spring Boot DevTools。创建好后，点右下角的 Open 按钮，打开到新的开发窗口。

配置好后，Java 代码保存时，默认自动更新部署。还是挺方便的。

### 部署到 Tomcat
注意 Tomcat Server 一定要和 SpringBoot 里使用的版本一致，否则部署成功后还是报错 404 找不到路径。

查看 SpringBoot 内置 Tomcat 版本的方法：
可查看C:\Users\用户.m2\repository\org\springframework\boot\spring-boot-dependencies\1.5.1.RELEASE\spring-boot-dependencies-1.5.1.RELEASE.pom。打开这个pom文件，搜索“tomcat.version” 会找到对应tomcat版本。如我的是
```xml
<tomcat.version>9.0.64</tomcat.version>

```
