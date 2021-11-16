FROM openjdk:8
COPY target/ target/
RUN  mv target/*.jar   target/user-store-mapping.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "target/user-store-mapping.jar"]