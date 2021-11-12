FROM openjdk:8
COPY  debit-notes/target/  target/
RUN  mv target/*.jar   target/debit-notes.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "target/debit-notes.jar"]
