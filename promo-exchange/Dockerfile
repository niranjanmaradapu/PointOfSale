FROM openjdk:8
COPY  target/  target/
RUN  mv target/*.jar   target/promo-exchange.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "target/promo-exchange.jar"]
