FROM maven:3.8.6-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17.0.1-slim
MAINTAINER Gl4di4tor
COPY --from=build /home/app/target/spring-infra.war /usr/local/lib/spring-infra.war
EXPOSE 8101
ENTRYPOINT ["java", "-jar", "/usr/local/lib/spring-infra.war"]