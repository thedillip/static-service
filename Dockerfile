FROM openjdk:8-jdk-alpine
MAINTAINER dillipfolio.web.app
COPY target/static-service.jar static-service.jar
ENTRYPOINT ["java","-jar","/static-service.jar"]