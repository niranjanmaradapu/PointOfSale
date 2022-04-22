FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/promo-exchange.jar
EXPOSE 8088
ENTRYPOINT ["java", "-Dspring.profiles.active=cloud", "-jar", "target/promo-exchange.jar"]
