FROM openjdk:20
EXPOSE 8090
ADD Jar/prefy-spring-boot-application.jar prefy-spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "/prefy-spring-boot-application.jar"]