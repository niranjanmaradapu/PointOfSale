FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/connection-pool.jar
EXPOSE 8085
ENTRYPOINT ["java", "-Dspring.profiles.active=cloud", "-jar", "target/connection-pool.jar"]