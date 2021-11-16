FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/customer-management.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "target/customer-management.jar"]