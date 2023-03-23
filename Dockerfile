#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11
COPY --from=build /target/static-service.jar static-service.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","static-service.jar"]