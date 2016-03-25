# slackbot-integration [![Build Status](https://travis-ci.org/fergusstrange/cloudbot.svg?branch=master)](https://travis-ci.org/fergusstrange/cloudbot)

Build with the ```build-docker.sh``` script to compile with Gradle, run tests and generate a Docker image named fergusstrange/cloudbot-slack-integration.

Run the docker image with the following

```
 docker run \
 -e "slackAuthToken=yourKeyHere" \
 -e "slackAdmins=adminUserName" \
 -e "awsDefaultImageId=ami-12345" \
 -e "awsDefaultSecurityGroup=GroupName" \
 -e "awsDefaultPlacement=eu-west-1c" \
 -e "awsAccessKey=aKey" \
 -e "awsSecretKey=anotherKey" \
 -e "awsDefaultRegion=eu-west-1" \
 -p 8080:8080 fergusstrange/cloudbot-slack-integration
```