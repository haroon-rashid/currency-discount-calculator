#currency-discount-calculator

#Prerequisites
Java 17,
Maven 3.6+
SonarQube server (optional for code quality reporting)


# Reports and UML Diagram is in the "Reports & UML Diagram" Folder

#=======> Scripts <=======

#Maven Build Scripts
./build-project.sh

#Run static code analysis:
./run-linting.sh

#Run tests and generate coverage reports:
./run-tests-and-coverage.sh


#=======> Using Exchange API <=======
Before Build Project first add your currency exchange API "application.properites" like 

# Base URL for the currency exchange API
currency.api.base-url=https://v6.exchangerate-api.com/v6/your-exchange-API-key/latest/

After Build and run the project imports belows Curl in Postman

#Login API

curl --location 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=342655C57EE48A922FD48DA08D96FB99' \
--data '{
    "username": "user",
    "password": "password"
}'


Calling above API in response you will get JWT Token and copy that token;

#Calculate API
 
Using the API past you token on "your-jwt-token" and call the API.

curl --location 'http://localhost:8080/api/calculate' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer your-jwt-token' \
--header 'Cookie: JSESSIONID=342655C57EE48A922FD48DA08D96FB99' \
--data '{
    "items": [
        { "name": "Apple", "category": "Grocery", "price": 200 },
        { "name": "Headphones", "category": "Electronics", "price": 583 }
    ],
    "totalAmount": 783,
    "userType": "CUSTOMER",
    "customerTenureYears": 3,
    "originalCurrency": "AED",
    "targetCurrency": "PKR"
}
'


 In above API "userType" wile be  in EMPLOYEE, AFFILIATE and CUSTOMER.




#I have done all bonus activities in this project:
Create build scripts using Maven or Gradle to:
o Build the project from the command line.
o Run static code analysis such as linting.
o Run unit tests and generate code coverage reports.
 Generate a SonarQube report for the code quality summary.
 Implement caching for exchange rates to reduce API calls.











