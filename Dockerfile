FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 18082

ARG JAR_FILE=target/starter-*.jar

ADD ${JAR_FILE} starter.jar


ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/starter.jar"]
