# Choose the smallest base image https://github.com/docker-library/docs/blob/master/openjdk/README.md#supported-tags-and-respective-dockerfile-links
FROM openjdk:11-slim
COPY ./target/*.jar ./app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar"]