FROM openjdk:8
ARG JAR_FILE = target/*.jar 
COPY  ${JAR_FILE} connection-pool.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "connection-pool.jar"]
