FROM openjdk:8-jre-alpine
ARG DOCKERIZE_VERSION
ENV APP_HOME /app

WORKDIR $APP_HOME

ADD target/*.jar ./app.jar
ADD  https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz dockerize.tar.gz
RUN tar xzf dockerize.tar.gz
RUN chmod +x dockerize
RUN rm dockerize.tar.gz
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]