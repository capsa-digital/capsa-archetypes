FROM openjdk:11.0.7-jre
MAINTAINER michael.lifschitz@gmail.com

WORKDIR /opt/app
ARG JAR_FILE
ARG INFO_APP_BUILD
ARG INFO_APP_ENV

COPY ${JAR_FILE} /opt/app/app.jar
ENV INFO_APP_BUILD=${INFO_APP_BUILD}
ENV SPRING_PROFILES_ACTIVE=${INFO_APP_ENV}

CMD java -jar /opt/app/app.jar