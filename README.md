![](https://github.com/OpenLiberty/open-liberty/blob/master/logos/logo_horizontal_light_navy.png)

# Async REST Sample
## This sample shows how to serve and consume RESTful services more efficiently using Asynchronous APIs from JAX-RS and MicroProfile

This sample represents a travel agency service that depends on three "remote" services: an airline service, a hotel service, and a car rental service.
These services are running local to the travel agency service for simplicity, but could very well be actual remote services. A [blog post](AsyncREST.asciidoc)
describes the asynchronous APIs used in this sample.

To run this sample, first [download](https://github.com/OpenLiberty/sample-async-rest/archive/master.zip) or clone this repo - to clone:
```
git clone git@github.com:OpenLiberty/sample-async-rest.git
```

From inside the sample-async-rest directory, build and start the application in Open Liberty with the following command:
```
mvn clean package liberty:run-server
```

The server will listen on port 9080 by default.  You can change the port (for example, to port 9081) by adding `mvn clean package liberty:run-server -DtestServerHttpPort=9081` to the end of the Maven command.

Once the server is started, you should be able to access the travel agency services using MicroProfile OpenAPI's UI at:
http://localhost:9080/openapi/ui

You can invoke the travel agency services by selecting from the `/jaxrs20/travel/cost`, `jaxrs21/travel/cost` or `mpRest/travel/cost` endpoints. 
The `/airline`, `/hotel`, and `/carRental` endpoints are the "remote" services.

You can also use curl to access the travel agency services - for example:
`curl -X GET "http://localhost:9080/AsyncRestSample/jaxrs20/travel/cost?from=New%20York&to=Kansas%20City&startDate=2019-01-01&returnDate=2019-01-15" -H  "accept: */*" -H "Loyalty-ID: 1234"`

`curl -X GET "http://localhost:9080/AsyncRestSample/jaxrs21/travel/cost?from=New%20York&to=Kansas%20City&startDate=2019-01-01&returnDate=2019-01-15" -H  "accept: */*" -H "Loyalty-ID: 1234"`

`curl -X GET "http://localhost:9080/AsyncRestSample/mpRest/travel/cost?from=New%20York&to=Kansas%20City&startDate=2019-01-01&returnDate=2019-01-15" -H  "accept: */*" -H "Loyalty-ID: 1234"`

should see output similar to:
![](https://github.com/OpenLiberty/sample-async-rest/raw/master/img/curl-output.png)


Press `Ctrl-C` from the command line to stop the server.

Please take a look at the source code that makes this possible.  If you run into any problems with the sample, please let us know by opening a new issue.

Thanks!

