# This will build the project in a container
FROM gradle:7.5.0-jdk17-alpine as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

# This will run the previously built artifact
FROM openjdk:17-alpine

ARG JAR_FILE=/home/gradle/src/build/libs/*.jar
WORKDIR /opt/app
COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]