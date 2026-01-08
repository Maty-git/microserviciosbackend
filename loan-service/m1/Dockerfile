FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} m1.jar
ENTRYPOINT ["java", "-jar", "m1.jar"]