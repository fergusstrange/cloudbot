#!/bin/bash
if [ ! -f build/libs/slackbot-integration.jar ]; then
    ./gradlew clean build
fi
docker build -t fergusstrange/cloudbot-slack-integration .