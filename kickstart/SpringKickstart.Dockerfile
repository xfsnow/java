FROM openjdk:18
COPY ./target/*.jar ./app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar"]
CMD ["app.jar"]