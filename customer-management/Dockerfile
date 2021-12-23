FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/customer-management.jar
EXPOSE 8082
ENTRYPOINT ["java", "-Dspring.profiles.active=cloud", "-jar", "target/customer-management.jar"]