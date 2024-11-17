#!/bin/bash

PROJECT_DIR=$(pwd)

echo "Running SonarQube analysis..."
mvn clean install verify sonar:sonar

echo "SonarQube analysis completed successfully."