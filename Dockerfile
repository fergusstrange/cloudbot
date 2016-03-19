FROM anapsix/alpine-java:8

EXPOSE 8080
COPY build/libs/slackbot-integration.jar /slackbot-integration.jar

CMD ["java", "-jar", "/slackbot-integration.jar"]