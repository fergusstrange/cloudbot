#!/bin/bash
./gradlew clean build && docker build -t infinityworks/cloudbot-slack-integration .