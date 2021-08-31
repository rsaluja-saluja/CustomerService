# CustomerService
Service to provide APIs 
* Fetch phone numbers for all customers
* Fetch phone numbers for particular customer
* Activate or Deactivate phone number(s) for customer(s)

# How to setup service 
* Clone service into your machine.
* Import project in SprintToolSuite and start the service (follow below API specifications).
* Use Curl (or tools like postman) to call API endpoints.
* To run unit tests, execute CustomerControllerTest as JUnit test.

# API Specifications
Below are some key points on usage of API. Open API-Specification.json into swagger hub for more details.
## Get All Customer PhoneNumbers (GET)
`http://<host:port>/customers/phonenumbers` is exposed API to fetch phone numbers for all customers.
* By default, API will return configured number of records(customerservice.noOfCustomersToSend property) in one call. It will append `nextCustomerToFetch` property in response to indicate request for next page for client. Currently this property is configured to value 2.
* Once client will find `nextCustomerToFetch`, it can be used as query parameter(customerIdToFetchFrom) to API(`http://<host:port>/customers/phonenumbers?customerIdToFetchFrom=<value>`) to fetch next set of records.
* API will not append `nextCustomerToFetch` property in response in case there are no further records to fetch (indicating last page).
* API will return HTTP 200 for success and HTTP 500 - Internal server error for failures

## Get Particular Customer Phone Number (GET)
`http://<host:port>/customers/{id}/phonenumbers' is exposed API to fetch phone numbers for particular customer.
* API will return HTTP 400 - Bad Request if customer id requested is in negative numbers (or zero)
* API will return HTTP 404 - Not found if customer id requested does not exist.
* API will return 200 for Success and HTTP 500 - Internal server error for failures.
 
## Activate / Deactivate customer phone number (PUT)
`http://<host:port>/customers/{id}/phonenumbers` is exposed API to activate/deactivate phone number(s) for customer.
* API need request body with fields `type` for type of Phone number and `activate` as boolean flag to activate or deactivate phone number.
* API will set activate status to true / false for all phone numbers for customer, if `type` field in passed as `ALL` in request body.
* API will return HTTP 400 - Bad Request if customer id requested is in negative numbers (or zero).* 
* API will return HTTP 400 - Bad Request if request body doesn't have both fields(type and activate).
* API will return HTTP 404 - Not Found if customer doesn't have provided phone number type(For example Home, Work, Emergency etc.).
* API will return HTTP 404 - Not Found if customer requested doesn't exists.
* API will return 200 for Success and HTTP 500 - Internal server error for failures


# Technical Documentation
Below is the technical layered design (folder strcuture) used to implement APIs 
* com\example\customerservice\model - Data Objects used for customer data
* com\example\customerservice\repo -  Database interaction layer
* com\example\customerservice\service - Service layer as facade to database
* com\example\customerservice\resource - Controller layer exposing REST API end points
* com\example\customerservice\error - Custom Exceptions and exception handlers
* com\example\customerservice\dto - Data object to receive API input
