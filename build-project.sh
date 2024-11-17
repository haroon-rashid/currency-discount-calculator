#!/bin/bash

PROJECT_DIR=$(pwd)

echo "Building the project using Maven..."

mvn clean install -DskipTests

echo "Build completed successfully."
