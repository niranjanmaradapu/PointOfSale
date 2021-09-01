FROM openjdk:8
ARG JAR_FILE=debit-notes/target/*.jar 
COPY  ${JAR_FILE} debit-notes.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "debit-notes.jar"]
