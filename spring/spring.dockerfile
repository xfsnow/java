# FROM tomcat:9.0.64-jdk17
FROM tomcat:9.0.64-jre17
COPY server.xml /usr/local/tomcat/conf
COPY ./target/*.war /usr/local/tomcat/webapps/
EXPOSE 80
CMD ["catalina.sh", "run"]