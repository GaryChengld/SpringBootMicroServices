FROM openjdk:8-jre-alpine
ENV APP_HOME /app
WORKDIR $APP_HOME

COPY target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]