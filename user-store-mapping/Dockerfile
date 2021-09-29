FROM openjdk:8
COPY  user-store-mapping/target/*  target/
RUN  mv target/*.jar   target/user-store-mapping.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "target/user-store-mapping.jar"]
