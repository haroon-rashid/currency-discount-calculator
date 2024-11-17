#!/bin/bash

PROJECT_DIR=$(pwd)

echo "Running static code analysis with Maven..."
mvn checkstyle:check

echo "Static code analysis completed."
