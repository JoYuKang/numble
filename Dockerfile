FROM openjdk:11-jdk

ARG JAR_FILE=*.jar
COPY ${JAR_FILE} numble-9.jar

ARG IDLE_PROFILE
ENV ENV_IDLE_PROFILE=$IDLE_PROFILE

COPY properties/application-redis.yml application-redis.yml

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${ENV_IDLE_PROFILE}", "-Dspring.config.location=classpath:/application.yml,application-redis.yml", "/numble-9.jar"]