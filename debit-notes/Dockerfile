FROM openjdk:8
#ARG JAR_FILE=connection-pool/target/*.jar 
COPY  connection-pool/target/*  target/
RUN  mv target/*.jar   target/debit-notes.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "target/debit-notes.jar"]
