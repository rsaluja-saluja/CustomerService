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
* open api-specifications into swagger hub

# Technical Documentation
Below is the technical layered design (folder strcuture) used to implement APIs 
* com\example\customerservice\model - Data Objects used for customer data
* com\example\customerservice\repo -  Database interaction layer
* com\example\customerservice\service - Service layer as facade to database
* com\example\customerservice\resource - Controller layer exposing REST API end points
* com\example\customerservice\error - Custom Exceptions and exception handlers
* com\example\customerservice\dto - Data object to receive API input
