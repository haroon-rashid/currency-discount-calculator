#!/bin/bash

PROJECT_DIR=$(pwd)

echo "Running unit tests..."
mvn test

echo "Generating code coverage report using JaCoCo..."
mvn jacoco:report

echo "Code coverage report generated at target/site/jacoco/index.html"

echo "Unit tests and code coverage report generation completed."
