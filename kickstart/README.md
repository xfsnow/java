# Spring Cloud 学习演练

## VS Code 配置 Spring Cloud 使用 maven 做包管理
参考
https://blog.csdn.net/qq_18335837/article/details/100566949

主要是在 pom.xml 里添加以下依赖
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

配置好后，Java 代码保存时，默认自动更新部署。还是挺方便的。

### 设置默认首页

### 修改运行端口为 80

修改配置文件 src/main/resources/application.properties
```
server.port=80
```
如果没有就添加一行。

### 使用 Spring Cloud 获取客户端IP
https://simplesolution.dev/spring-boot-web-get-client-ip-address

## 部署到 Azure App Service
https://suthesana.medium.com/how-to-deploy-spring-boot-application-with-azure-app-service-e41b15f44a06

### 当前 azure-webapp-maven-plugin 最新版是
https://github.com/microsoft/azure-maven-plugins/releases/tag/azure-webapp-maven-plugin-v2.2.2
```
					 <javaVersion>Java 17</javaVersion>
					 <webContainer>Java SE</webContainer>
```
可取值到以下文档查
https://aka.ms/maven_webapp_runtime#webcontain

```
<configuration>
				   <schemaVersion>V2</schemaVersion>
				   <resourceGroup>azure-multi-region</resourceGroup>
				   <appName>spring-hello</appName>
				   <region>eastasia</region>
				   <pricingTier>f1</pricingTier>
				   <runtime>
					 <os>linux</os>
					 <javaVersion>Java 17</javaVersion>
					 <webContainer>Java SE</webContainer>
				   </runtime>
				   <!-- Begin of App Settings  -->
				   <appSettings>
					  <property>
							<name>JAVA_OPTS</name>
							<value>-Dserver.port=80</value>
					  </property>
				   </appSettings>
				   <!-- End of App Settings  -->
				   <deployment>
					 <resources>
					   <resource>
						 <directory>${project.basedir}/target</directory>
						 <includes>
						   <include>*.jar</include>
						 </includes>
					   </resource>
					 </resources>
				   </deployment>
				  </configuration>
```

昨天参考上述 medium 的博客，使用代码方式部署到 App Service，一直不行。

## 以 Docker 镜像方式部署到 App Service
今天再试一下官方文档，使用 Docker 模式部署。推送到 ACR 有个额外的好处，未来还能从这里再部署到 AKS。
https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/deploy-spring-boot-java-app-on-linux

文档中下面这行命令其实是2条
```
az acr login -n RegistryName && mvn compile jib:build
```
我分开来执行
```
az acr login -n RegistryName
```
报错
```
You may want to use 'az acr login -n RegistryName --expose-token' to get an access token, which does not require Docker to be installed.
```
那就按提示加上  `--expose-token` 参数呗。
```
az acr login -n RegistryName --expose-token
```

再执行就成了。最后记得先把本地Docker Desktop 运行起来，再执行

```
mvn compile jib:build
```
否则会报错验证失败。

推送到ACR 终于成功，但是部署到 Azure App Service 或 Azure Kubernetes Service 都不行。App Service 报错不能拉起镜像。AKS 中 pod 日志显示
```
Error: Could not find or load main class fun.snowpeak.kickstart.KickstartApplication

```

## 自己手工构建 Docker 镜像
docker build -f SpringKickstart.Dockerfile -t kickstart:latest -t kickstart:0.1 .

docker run -d -p 8080:80 --name kickstart kickstart

运行不了，查看一下日志
```
docker logs kickstart
Exception in thread "main" java.lang.UnsupportedClassVersionError: fun/snowpeak/kickstart/KickstartApplication has been compiled by a more recent version of the Java Runtime (class file version 62.0), this version of the Java Runtime only recognizes class file versions up to 55.0
        at java.base/java.lang.ClassLoader.defineClass1(Native Method)

```
Dockerfile 中基础镜像必须和 Spring Boot 项目选择的 JDK 版本一致。把之前的  `FROM openjdk:11`  改成  `FROM openjdk:18`  就好了。

## 推送到 Azure Container Registry

然后推送到 Azure 中国区的 Container Registry
```
az cloud set -n AzureChinaCloud
az login
az acr login --name snowpeak
docker tag kickstart:0.1 snowpeak.azurecr.cn/kickstart:0.1
docker push snowpeak.azurecr.cn/kickstart:0.1
docker tag kickstart:0.1 snowpeak.azurecr.cn/kickstart:latest
docker push snowpeak.azurecr.cn/kickstart:latest
```

## TODO
发现 openjdk 基础镜像太大。计划改成用 tomcat 做基础镜像，但是得改 Spring Boot 项目的整体配置。
