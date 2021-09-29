FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/connection-pool.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "target/connection-pool.jar"]
