# Spring Cloud 学习演练

## VS Code 配置 Spring Cloud 使用 maven 做包管理
参考
https://blog.csdn.net/led1114/article/details/122650051

Ctrl+Shift+P，然后输入 Spring Initializr: Create a Maven project。前几步都选择提示的默认选项即可，到 Specify package type 时，选择 War。Java version 选当前最新的 18。Choose dependencies 选 Spring Boot DevTools。创建好后，点右下角的 Open 按钮，打开到新的开发窗口。

配置好后，Java 代码保存时，默认自动更新部署。还是挺方便的。

## 调整为运行在网站根目录
主要是 HelloController.java 中
```java
@RestController
public class HelloController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
    ...
```
配合 server.xml
`<Host>` 节点下增加
```xml
<!-- Set this web app to run on root of the site. -->
<Context docBase="/usr/local/tomcat/webapps/spring-0.0.1-SNAPSHOT" path="/" reloadable="true"/>
```
其中 docBase的值是 Docker 基础镜像中 Tomcat 站点路径 /usr/local/tomcat/webapps/ 再加上当前 SpringCloud 项目打包成的 spring-0.0.1-SNAPSHOT 制品名，不带后缀。这个制品名字是由 pom.xml 文件配置指定的：
```xml
	<artifactId>spring</artifactId>
	<version>0.0.1-SNAPSHOT</version>
```

## 部署到 Tomcat
注意 Tomcat Server 一定要和 SpringBoot 里使用的版本一致，否则部署成功后还是报错 404 找不到路径。

查看 SpringBoot 内置 Tomcat 版本的方法：
可查看C:\Users\用户.m2\repository\org\springframework\boot\spring-boot-dependencies\1.5.1.RELEASE\spring-boot-dependencies-1.5.1.RELEASE.pom。打开这个pom文件，搜索“tomcat.version” 会找到对应tomcat版本。如我的是
```xml
<tomcat.version>9.0.64</tomcat.version>

```

### 80 端口
修改 server.xml 中的  Connector 中 port 属性值为80。
```xml
    <Connector port="80" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
```


# 构建成 docker 镜像
## 编辑 Dockerfile 文件
这里我指定了文件名 [spring.dockerfile](spring.dockerfile) 。

先使用默认配置构建一个
```
docker build -f spring.dockerfile -t spring-hello:0.1 -t spring-hello:latest .
```

本地运行一下
```
docker run -d -p 8080:80 --name spring-hello spring-hello
```
可以正常运行，但是访问报 404

最后排查，还是 Spring Cloud 中的 JDK 和 Tomcat 得和 Docker 基础镜像中的 JDK 和 Tomcat 版本一致。由于 `FROM tomcat:9.0.64-jdk17` 现在最高就是 JDK 17 了，所以回来改 Spring Cloud 项目中的 pom.xml
```
<properties>
	<java.version>17</java.version>
</properties>
```
改成 `<java.version>17</java.version>`

## 推送到 Azure Container Registry
然后推送到 Azure 中国区的 Container Registry
```bash
az cloud set -n AzureChinaCloud
az login
az acr login --name snowpeak
docker tag spring-hello:0.1 snowpeak.azurecr.cn/spring-hello:0.1
docker push snowpeak.azurecr.cn/spring-hello:0.1
docker tag spring-hello:0.1 snowpeak.azurecr.cn/spring-hello:latest
docker push snowpeak.azurecr.cn/spring-hello:latest
```


## 镜像体积的反复尝试
第一次都构建好了，也能正常运行了，发现 docker 镜像比之前使用 jar 包构建基于 jdk 的镜像体积还要大。在 dockerfile 中 `FROM tomcat:9.0.64-jdk17` 时，最终构建出的镜像体积有 500.28MB，而原来使用 jar 包的镜像体积只有 481.91MB。于是又仔细挑选了几个基础镜像。目前试出来能满足功能并且体积尽量小的是 `FROM tomcat:9.0.64-jre17`，现在构建出的镜像有 309.75MB。

