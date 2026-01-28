# syntax=docker/dockerfile:1
FROM --platform=linux/amd64 maven:3.9.12-eclipse-temurin-21-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:21-jdk-alpine-3.23 AS runtime
LABEL org.opencontainers.image.authors="Gl4di4tor"
COPY --from=build /home/app/target/spring-infra.war /usr/local/lib/spring-infra.war
EXPOSE 8101
ENTRYPOINT ["java", "-jar", "/usr/local/lib/spring-infra.war"]