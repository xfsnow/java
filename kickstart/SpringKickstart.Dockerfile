FROM openjdk:18-slim
COPY ./target/*.jar ./app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar"]