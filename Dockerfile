FROM openjdk:20
EXPOSE 8090
ADD Jar/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "/spring-boot-application.jar"]