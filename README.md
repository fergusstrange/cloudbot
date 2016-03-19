# slackbot-integration [![Build Status](https://travis-ci.org/fergusstrange/cloudbot.svg?branch=master)](https://travis-ci.org/fergusstrange/cloudbot)

Build with the ```build-docker.sh``` script to compile with Gradle, run tests and generate a Docker image named fergusstrange/cloudbot-slack-integration.

Run the docker image with the following

```
 docker run -e "slackAuthToken=yourKeyHere" -p 8080:8080 fergusstrange/cloudbot-slack-integration
```