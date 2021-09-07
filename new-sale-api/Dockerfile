FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/new-sale-api.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "target/new-sale-api.jar"]
