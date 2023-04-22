FROM openjdk:20
ARG JAR_FILE=target/*.jar
COPY ./Jar/prefy-spring-boot-application.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]