FROM openjdk:11-jdk

ARG JAR_FILE=*.jar
COPY ${JAR_FILE} numble-9.jar

ARG IDLE_PROFILE
ENV ENV_IDLE_PROFILE=$IDLE_PROFILE

COPY properties/application-session.yml application-session.yml
COPY properties/application-db.yml application-db.yml
COPY properties/application-jpa.yml application-jpa.yml
COPY properties/application-oauth2.yml application-oauth2.yml
COPY properties/application-api.yml application-api.yml

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${ENV_IDLE_PROFILE}", "-Dspring.config.location=classpath:/application.yml,application-session.yml,application-db.yml,application-jpa.yml,application-oauth2.yml,application-api.yml", "/numble-9.jar"]