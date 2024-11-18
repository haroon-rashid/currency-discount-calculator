# Currency Discount Calculator

## Project Overview

The **Currency Discount Calculator** is a Spring Boot application designed to interact with a third-party currency exchange API for fetching real-time exchange rates. It computes the total payable amount for a bill in a chosen currency after applying relevant discounts. This API enables users to submit a bill in one currency and receive the payable amount in a different currency.

---

## Key Capabilities

- **Real-Time Currency Exchange**: Integrates with a third-party API to retrieve up-to-date exchange rates.
- **Dynamic Discount Mechanism**: Calculates discounts tailored to user profiles and rules:
  - `30%` for employees.
  - `10%` for affiliates.
  - `5%` for loyal customers with over 2 years of tenure.
  - `$5` reduction for every `$100` spent.
  - Grocery items excluded from percentage-based discounts.
  - Only one percentage discount allowed per bill.
- **Cross-Currency Bill Conversion**: Handles seamless conversion of bill amounts between different currencies.
- **JWT Security Framework**: Uses JWT authentication for secure, role-based access.

---

## Additional Resources

- **Reports and UML Diagrams**: Available in the `Reports & UML Diagram` folder.

---

## Prerequisites

- **Java**: 17
- **Maven**: 3.6+
- **SonarQube Server** (optional for code quality reporting)

---

## Build and Deployment

### Maven Build Scripts

1. **Build the Project**:
   ```bash
   ./build-project.sh```
   
2. **Run Static Code Analysis**:
   ```bash
   ../run-linting.sh```
  
   
3. **Run Tests and Generate Coverage Reports:**:
   ```bash
   ./build-project.sh```
   


## Configuration: Currency Exchange API

To use the currency exchange API, update the `application.properties` file in your Spring Boot project. Add the following property with your API key:

```properties

# Base URL for the currency exchange API
currency.api.base-url=https://v6.exchangerate-api.com/v6/your-exchange-API-key/latest/ 

```


## API Usage

### 1. **Login API**

To authenticate and obtain a JWT token, use the login API:

```bash
curl --location 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=342655C57EE48A922FD48DA08D96FB99' \
--data '{
    "username": "user",
    "password": "password"
}'

```

## Response

```bash
{
    "token": "your-jwt-token"
}

```

The response will include a JWT token. Copy this token to use in subsequent API calls.

---

## 2. Calculate API

To calculate the payable amount, use the **Calculate API**. Include the JWT token obtained from the login API in the `Authorization` header.

### Example API Call:
```bash
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
}' 

```



## Response

```bash
{
    "items": [
        {
            "name": "Apple",
            "category": "Grocery",
            "price": 200
        },
        {
            "name": "Headphones",
            "category": "Electronics",
            "price": 583
        }
    ],
    "totalAmount": 783,
    "userType": "CUSTOMER",
    "customerTenureYears": 3,
    "originalCurrency": "AED",
    "targetCurrency": "PKR",
    "percentageDiscount": 2204.23,
    "discountsOnEveryHundredUSD": 2777.405064,
    "totalPayableAmount": 54226.31,
    "totalDiscount": 4981.64
} 

```


## Bonus Activities Implemented

### Build Scripts:
- Created Maven build scripts to build the project from the command line.
- Added scripts for:
  - Static code analysis.
  - Running tests.
  - Generating coverage reports.

### SonarQube Integration:
- Integrated **SonarQube** for detailed code quality reporting.

### Caching:
- Implemented caching for exchange rates to:
  - Minimize API calls.
  - Improve overall efficiency.

---


## Technologies Used

- **Spring Boot**: Framework for building REST APIs.
- **Postman**: API documentation.
- **JWT**: JWT authentication.
- **Webclient**: HTTP client for API integration.
- **Maven**: Build tool.
- **JUnit & Mockito**: Unit testing.
- **SonarQube** : Detailed code quality reporting.
- **JaCoCo**: Code coverage reporting.

---