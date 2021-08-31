# CustomerService
Service to provide APIs 
* fetch phone numbers for all customers
* fetch phone numbers for particular customer
* activate or deactivate phone number for customer(s)

# How to setup service 
* clone service into your machine
* import project in SprintToolSuite and start the service (follow below API specifications)
* use Curl (or tools like postman) to call API endpoints
* To run unit tests, execute CustomerControllerTest as JUNit test

# API Specifications
Below are some key points on usage of API. open api-specifications into swagger hub for more details.
## GetCustomerPhoneNumbers (GET)
http://xxxx/ is exposed as API to fetch phone numbers for all customers.
* By default, API will return configured number of records in one call. It will append `next` to indicate request for next page for caller
* Once caller will find ``, it can be used as query parameter to API to fetch next set of records.
* API will not append `next` in case there are no further records to fetch (indicating last page)
* API will return HTTP 200 for success and HTTP 500 - Internal server error for failures

## Get Customer Phone Number (GET)
http://xxxx/ is exposed API to fetch phone numbers for particular customer
* API will return HTTP 400 - Bad Request if customer id requested is in negative numbers (or zero)
* API will return HTTP 404 - Not found if customer id requested does not exist
* API will return 200 for Success and HTTP 500 - Internal server error for failures
 
## Activate / Deactivate customer phone number (PUT)
http://xxxx/ is exposed API to activate phone numbers for customer(s)
* API will return HTTP 400 - Bad Request if customer id requested is in negative numbers (or zero)
* API need request body with fields `type` for type of Phone number and `activate` as boolean flag to activate or deactivate phone number
* API will return HTTP 400 - Bad Request if request body is not provided
* API will return HTTP 400 - Bad Request if phone number type (Home, Business, Emergency, All etc.) in  request body is not provided
* API will return HTTP 400 - Bad Request if phone number type (Home, Business, Emergency, All etc.) in  request body is not provided
* API will set activate status to true / false for all phone numbers for customer, if `type` field in passed as `ALL` in request body.
* HTTP 500 - Internal server error will return for failures


# Technical Documentation
Below is the technical layered design (folder strcuture) used to implement APIs 
* com\example\customerservice\model - Data Objects used for customer data
* com\example\customerservice\repo -  Database interaction layer
* com\example\customerservice\service - Service layer as facade to database
* com\example\customerservice\resource - Controller layer exposing REST API end points
* com\example\customerservice\error - Custom Exceptions and exception handlers
* com\example\customerservice\dto - Data object to receive API input
